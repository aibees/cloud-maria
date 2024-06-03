package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.account.service.AccountServiceCommon;
import com.aibees.service.maria.common.PathBuilder;
import com.aibees.service.maria.common.StringUtils;
import com.aibees.service.maria.common.vo.ResponseData;
import com.aibees.service.maria.multipart.domain.dto.FileImageDisplayRes;
import com.aibees.service.maria.multipart.domain.dto.FileImageReq;
import com.aibees.service.maria.multipart.domain.entity.FileImage;
import com.aibees.service.maria.multipart.domain.entity.ImageFileEntity;
import com.aibees.service.maria.multipart.domain.dto.FileCondition;
import com.aibees.service.maria.multipart.domain.dto.ImageFileCondition;
import com.aibees.service.maria.multipart.domain.repo.FileImageRepo;
import com.aibees.service.maria.multipart.domain.repo.ImageFileRepository;
import com.aibees.service.maria.multipart.domain.vo.ImageFileVo;
import com.aibees.service.maria.multipart.service.FileService;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aibees.service.maria.common.CONSTANT.FILE_HOME;
import static com.aibees.service.maria.common.CONSTANT.IMAGE_HOME;

@Service
@AllArgsConstructor
@Slf4j
public class FileImageService extends AccountServiceCommon implements FileService {

    private final ImageFileRepository imageRepository;
    private final FileImageRepo fileImageRepo;

    public List<ImageFileVo> getImageListByCondition(ImageFileCondition condition) {
        if(condition.categoryAndYmCondChk()) {
            return getImageListByCategoryAndYm(
                    condition.getCategory(),
                    condition.getYm()
            );
        } else if(condition.categoryCondChk()) {
            return getImageListByCategory(
                    condition.getCategory()
            );
        } else {
            return null;
        }
    }

    public ResponseEntity<ResponseData> getDisplayImage(FileImageReq param) {
        if(param.getShotTime() == null) {
            param.setShotTime(LocalDateTime.now());
        }

        List<FileImageDisplayRes> imageList = fileImageRepo.selectDisplayImageList(param)
                .stream()
                .map(entity ->
                        FileImageDisplayRes.builder()
                                .id(entity.getId())
                                .ym(entity.getYm())
                                .imageId(entity.getImageId())
                                .width(entity.getWidth())
                                .height(entity.getHeight())
                                .shotTime(entity.getShotTime())
                                .fullPath(entity.getFullPath())
                                .displayYn(entity.getDisplayYn())
                                .build()
                ).collect(Collectors.toList());

        return successResponse(imageList);
    }


    private List<ImageFileVo> getImageListByCategory(String category) {
        return imageRepository.findByCategory(category)
                .stream().map(this::imageFileConverter)
                .collect(Collectors.toList());
    }

    private List<ImageFileVo> getImageListByCategoryAndYm(String category, String ym) {
        return imageRepository.findByCategoryAndYm(category, ym)
                .stream().map(this::imageFileConverter)
                .collect(Collectors.toList());
    }

    @Override
    public Resource getResource(FileCondition param) throws IOException {
        ImageFileCondition imageParam = (ImageFileCondition) param;
        log.info("ImageFileCondition : " + imageParam.toString());
        List<ImageFileEntity> imageEntities = imageRepository.findByCategoryAndYmAndNumber(
                  imageParam.getCategory().toUpperCase()
                , imageParam.getYm()
                , imageParam.getNumber()
        );
        ImageFileEntity imageEntity = imageEntities.get(0);

        String fileName = StringUtils.UuidNumberFormat(imageParam.getNumber()) + "." + imageEntity.getExt();
        String filePath = new PathBuilder.start()
                                         .setBasePath(FILE_HOME + IMAGE_HOME)
                                         .addPath(imageParam.getCategory())
                                         .addPath(imageParam.getYm())
                                         .addPath(fileName)
                                         .end();

        showMetadata(filePath);

        return new FileSystemResource(
                new File(filePath).toPath()
        );
    }

    @Override
    public String getUUID(String type) {
        return null;
    }

    private ImageFileVo imageFileConverter(ImageFileEntity entity) {
        return ImageFileVo.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .ym(entity.getYm())
                .number(entity.getNumber())
                .filename(entity.getFilename())
                .createAt(entity.getCreatedAt())
                .build();
    }

    private String getFilePath(ImageFileCondition param) {
        List<String> pathes = new ArrayList<>();
        pathes.add(param.getCategory());
        pathes.add(param.getYm());

        return String.join("/", pathes);
    }

    private void showMetadata(String filename) {
        DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
        try {
            File file = new File(filename);
            Metadata meta = JpegMetadataReader.readMetadata(file);

            ExifIFD0Directory ifdDir = meta.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if(!Objects.isNull(ifdDir)) {
                String takeTimeStr = ifdDir.getString(ExifIFD0Directory.TAG_DATETIME);
                LocalDateTime takeTime = LocalDateTime.parse(takeTimeStr, form);

                System.out.println("filename : " + filename);
                System.out.println("orientation time : " + takeTime.toString());
            } else {
                System.out.println("pic Exif is null");
            }
        } catch (IOException e) {
            System.out.print(e.getMessage());
        } catch (IllegalArgumentException ae) {
            System.out.println(ae.getMessage());
        } catch (JpegProcessingException e) {
            System.out.println("JpegProcessingException : " + e.getMessage());
        }
    }
}

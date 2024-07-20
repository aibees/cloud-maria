package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.ServiceCommon;
import com.aibees.service.maria.multipart.converter.FileImageConverter;
import com.aibees.service.maria.multipart.domain.dto.FileImageDisplayRes;
import com.aibees.service.maria.multipart.domain.dto.FileImageReq;
import com.aibees.service.maria.multipart.domain.dto.FileCondition;
import com.aibees.service.maria.multipart.domain.dto.ImageFileCondition;
import com.aibees.service.maria.multipart.domain.entity.FileImage;
import com.aibees.service.maria.multipart.domain.repo.FileImageRepository;
import com.aibees.service.maria.multipart.domain.vo.ImageFileVo;
import com.aibees.service.maria.multipart.service.FileService;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FileImageService extends ServiceCommon implements FileService {

    private final FileImageRepository fileImageRepo;
    private final String WIDE = "wide";
    private final String NARR = "narrow";

    public List<ImageFileVo> getImageListByCondition(ImageFileCondition condition) {
        if (condition.categoryAndYmCondChk()) {
            return getImageListByCategoryAndYm(
                    condition.getCategory(),
                    condition.getYm()
            );
        } else if (condition.categoryCondChk()) {
            return getImageListByCategory(
                    condition.getCategory()
            );
        } else {
            return null;
        }
    }

    public ResponseEntity<ResponseData> getDisplayImage(FileImageReq param) {
        if (param.getShotTime() == null) {
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
                                .fullPath(entity.getFullPath())
                                .displayYn(entity.getDisplayYn())
                                .build()
                ).collect(Collectors.toList());

        return successResponse(imageList);
    }


    private List<ImageFileVo> getImageListByCategory(String category) {
        return fileImageRepo.findByCategory(category)
                .stream().map(FileImageConverter::toVo)
                .collect(Collectors.toList());
    }

    private List<ImageFileVo> getImageListByCategoryAndYm(String category, String ym) {
        return fileImageRepo.findByCategoryAndYm(category, ym)
                .stream().map(FileImageConverter::toVo)
                .collect(Collectors.toList());
    }

    @Override
    public Resource getResource(FileCondition condition) throws Exception {
        return null;
    }

    @Override
    public String getUUID(String type) {
        return null;
    }

    public ResponseEntity<ResponseData> getSNSImage(long id) {
        Optional<FileImage> imageData = fileImageRepo.findById(id);

        if(imageData.isEmpty()) {
            return failedResponse(new NullPointerException("ImageData is None"));
        } else {
            FileImage img = imageData.get();
            return successResponse(createTmpImage(img));
        }
    }

    private Map<String, Object> createTmpImage(FileImage image) {
        String[] fileName = image.getImageId().split("\\.");

        String sSpeed = image.getExposureTime() + "s";
        String fValue = "F" + image.getFNumber();
        String iso = "ISO " + image.getIsoRating();
        String focalLen = image.getFocalLen() + "mm";

        String exif = String.join(" | ", fValue, sSpeed, iso);
        String equip = image.getMakeModel() + " + " + focalLen + "(" + image.getLensModel() + ")";

        String rootDir = "/app/maria/tmp/";
//        String rootDir = "D:\\";

        try {
            BufferedImage background = ImageIO.read(new File(rootDir + "background.jpg"));
            BufferedImage engraving = ImageIO.read(new File(rootDir + "engrave.png"));
            BufferedImage picture = ImageIO.read(new File(image.getFullPath()));

            String picType = picture.getWidth() > picture.getHeight() ? WIDE : NARR;

            int size = 6000;
            int resizeConst = size * 9 / 10;
            int resizingWidth = 0;
            int resizingHeight = 0;

            if(WIDE.equals(picType)) {
                resizingWidth = resizeConst;
                resizingHeight = picture.getHeight() * resizeConst / picture.getWidth();
            } else {
                resizingWidth = picture.getWidth() * resizeConst / picture.getHeight();
                resizingHeight = resizeConst;
            }

            Image resizedPic = picture.getScaledInstance(resizingWidth, resizingHeight, Image.SCALE_SMOOTH);

            BufferedImage mergedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();

            graphics.setBackground(Color.WHITE);

            graphics.drawImage(background, 0, 0, null);
            graphics.drawImage(resizedPic, (size-resizingWidth)/2, (size-resizingHeight)/2, null);
            graphics.drawImage(engraving, 5700-engraving.getWidth(), 5700-engraving.getHeight(), null);

            int fontSize = 50;

            graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
            graphics.setColor(Color.BLACK);
            graphics.drawString(exif, (size-resizingWidth)/2, (size+resizingHeight)/2 + fontSize*2);
            graphics.drawString(equip, (size-resizingWidth)/2, (size+resizingHeight)/2 + 170);

            ImageIO.write(mergedImage, "jpg", new File(rootDir + "tmp_" + image.getImageId()));

            return ImmutableMap.of(
                    "status", "success",
                    "filePath", rootDir + "tmp_" + image.getImageId()
            );
        } catch (IOException e) {
            e.printStackTrace();
            return ImmutableMap.of("status", "error", "errMsg", e.getMessage());
        }
    }
}

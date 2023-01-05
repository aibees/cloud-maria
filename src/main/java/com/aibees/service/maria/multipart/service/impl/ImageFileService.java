package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.common.StringUtils;
import com.aibees.service.maria.multipart.dao.entity.ImageFileEntity;
import com.aibees.service.maria.multipart.dao.obj.FileCondition;
import com.aibees.service.maria.multipart.dao.obj.ImageFileCondition;
import com.aibees.service.maria.multipart.dao.repo.ImageFileRepository;
import com.aibees.service.maria.multipart.dao.vo.ImageFileVo;
import com.aibees.service.maria.multipart.dao.vo.ResourceVo;
import com.aibees.service.maria.multipart.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.aibees.service.maria.common.CONSTANT.FILE_HOME;

@Service
@AllArgsConstructor
public class ImageFileService implements FileService {

    private final ImageFileRepository imageRepository;

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
        System.out.println(imageParam.toString());

        String file = FILE_HOME;

        return new FileSystemResource(
                new File(file).toPath()
        );
    }

    @Override
    public String getUUID(String type) {
        return null;
    }

    private ImageFileVo imageFileConverter(ImageFileEntity entity) {
        return ImageFileVo.builder()
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
}

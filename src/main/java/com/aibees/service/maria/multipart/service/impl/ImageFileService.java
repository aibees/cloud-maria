package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.multipart.dao.entity.ImageFileEntity;
import com.aibees.service.maria.multipart.dao.obj.ImageFileCondition;
import com.aibees.service.maria.multipart.dao.repo.ImageFileRepository;
import com.aibees.service.maria.multipart.dao.vo.ImageFileVo;
import com.aibees.service.maria.multipart.dao.vo.ResourceVo;
import com.aibees.service.maria.multipart.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResourceVo getResource(String path, String name) {
        return null;
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
}

package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.ServiceCommon;
import com.aibees.service.maria.multipart.converter.FileImageConverter;
import com.aibees.service.maria.multipart.domain.dto.FileImageDisplayRes;
import com.aibees.service.maria.multipart.domain.dto.FileImageReq;
import com.aibees.service.maria.multipart.domain.dto.FileCondition;
import com.aibees.service.maria.multipart.domain.dto.ImageFileCondition;
import com.aibees.service.maria.multipart.domain.repo.FileImageRepository;
import com.aibees.service.maria.multipart.domain.vo.ImageFileVo;
import com.aibees.service.maria.multipart.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FileImageService extends ServiceCommon implements FileService {

    private final FileImageRepository fileImageRepo;

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

}

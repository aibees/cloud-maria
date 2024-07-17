package com.aibees.service.maria.multipart.converter;

import com.aibees.service.maria.multipart.domain.entity.FileImage;
import com.aibees.service.maria.multipart.domain.vo.FileVo;
import com.aibees.service.maria.multipart.domain.vo.ImageFileVo;

public class FileImageConverter {

    public static ImageFileVo toVo(FileImage entity) {
        return ImageFileVo.builder()
                .id(entity.getId())
                .ym(entity.getYm())
                .imageId(entity.getImageId())
                .filename(entity.getFilename())
                .width(entity.getWidth())
                .height(entity.getHeight())
                .displayYn(entity.getDisplayYn())
                .makeModel(entity.getMakeModel())
                .ext(entity.getExt())
                .software(entity.getSoftware())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .fullPath(entity.getFullPath())
                .exposureTime(entity.getExposureTime())
                .fNumber(entity.getFNumber())
                .isoRating(entity.getIsoRating())
                .focalLen(entity.getFocalLen())
                .lensModel(entity.getLensModel())
                .infoLock(entity.getInfoLock())
                .build();
    }
}

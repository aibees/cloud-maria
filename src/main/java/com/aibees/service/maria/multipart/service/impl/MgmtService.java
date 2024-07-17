package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.common.StringUtils;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.ServiceCommon;
import com.aibees.service.maria.multipart.converter.FileImageConverter;
import com.aibees.service.maria.multipart.domain.dto.FileImageReq;
import com.aibees.service.maria.multipart.domain.dto.PageableDto;
import com.aibees.service.maria.multipart.domain.repo.FileImageRepository;
import com.aibees.service.maria.multipart.domain.vo.ImageFileVo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MgmtService extends ServiceCommon {

    private final FileImageRepository fileImageRepository;

    public ResponseEntity<ResponseData> getFileImageMgmt(PageableDto page) {

        FileImageReq req = FileImageReq.builder()
                .searchSize(page.getSearchSize())
                .build();
        List<ImageFileVo> imageList = fileImageRepository.selectDisplayImageList(req)
                .stream()
                .map(entity -> {
                    ImageFileVo vo = FileImageConverter.toVo(entity);
                    if(StringUtils.isNotNull(vo.getYm())) {
                        vo.setYm(vo.getYm().substring(0, 4) + "/" + vo.getYm().substring(4));
                    }

                    return vo;
                })
                .collect(Collectors.toList());

//        List<ImageFileVo> getAllList = fileImageRepository.findAll()
//                .stream()
//                .map(entity -> {
//                    ImageFileVo vo = FileImageConverter.toVo(entity);
//                    if(StringUtils.isNotNull(vo.getYm())) {
//                        vo.setYm(vo.getYm().substring(0, 4) + "/" + vo.getYm().substring(4));
//                    }
//
//                    return vo;
//                })
//                .collect(Collectors.toList());

        return successResponse(imageList);
    }
}

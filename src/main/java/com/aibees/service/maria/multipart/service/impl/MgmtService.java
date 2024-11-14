package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.common.utils.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import java.awt.image.BufferedImage;
import java.io.File;
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

    public void convertFile(List<MultipartFile> imgFiles) {
        for(MultipartFile mfile : imgFiles) {
            if(mfile.getSize() < 1000000) {
                continue;
            }

            try {
                long size = mfile.getSize();
                long 배율 = size / 1000000;

                File file = new File("tmp_" + mfile.getOriginalFilename());
                mfile.transferTo(file);
                System.out.println(file.getPath());
                BufferedImage inputImage = ImageIO.read(file);

                ImageWriter wr = ImageIO.getImageWritersByFormatName("jpg").next();

                File output = new File("/home1/aibees/tmp_" + mfile.getOriginalFilename());
                ImageOutputStream outputStream = ImageIO.createImageOutputStream(output);
                wr.setOutput(outputStream);

                ImageWriteParam params = wr.getDefaultWriteParam();
                params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                params.setCompressionQuality((float)(1/배율));

                wr.write(null, new IIOImage(inputImage, null, null), params);

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}

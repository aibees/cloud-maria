package com.aibees.service.maria.multipart.controller.impl;

import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.multipart.controller.FileController;
import com.aibees.service.maria.multipart.domain.dto.FileImageReq;
import com.aibees.service.maria.multipart.domain.dto.ImageFileCondition;
import com.aibees.service.maria.multipart.domain.vo.ImageFileVo;
import com.aibees.service.maria.multipart.service.impl.FileImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/image")
public class ImageController implements FileController {

    private final FileImageService fileImageService;

    @GetMapping({ "/list/{category}", "/list/{category}/{ym}" })
    public ResponseEntity<List<ImageFileVo>> getImageListByCondition(ImageFileCondition params) {

        params.setCategory(params.getCategory().toUpperCase());

        return ResponseEntity.ok().body(
            fileImageService.getImageListByCondition(
                    ImageFileCondition
                            .builder()
                            .category(params.getCategory())
                            .ym(params.getYm())
                            .build()
            )
        );
    }

    @PostMapping("/display")
    public ResponseEntity<ResponseData> getDisplayImageList(@RequestBody FileImageReq params) {

        return fileImageService.getDisplayImage(params);
    }

    /**
     * 이미지 Resource 조회 (실제 이미지)
     * @param params
     * @return Resource
     */
    @GetMapping(value="/{category}/{ym}/{number}")
    public ResponseEntity<Resource> getImageResource(ImageFileCondition params) {

        return ResponseEntity
               .ok()
               .contentType(MediaType.parseMediaType(MediaType.IMAGE_PNG_VALUE))
               .body(null);
    }

    public ResponseEntity<Void> uploadResource(@RequestParam MultipartFile file, @RequestParam String data) {
        return null;
    }

    @CrossOrigin(origins = {"http://localhost:7077"})
    @PostMapping("/upload")
    public String uploadFiles(@RequestPart MultipartFile file) {
        if(Objects.isNull(file))
            System.out.println("file is null... WHY??");
        log.info("======== FILE UPLOAD ===========");
        log.info("file name : " + file.getOriginalFilename());
        log.info("file size : " + file.getSize());
        log.info("file meta : " + file.getContentType());

        return "OK";
    }

    @GetMapping("/sns/{id}")
    public ResponseEntity<ResponseData> createImageForSNS(@PathVariable("id") String id) {
        return fileImageService.getSNSImage(Long.parseLong(id));
    }
}

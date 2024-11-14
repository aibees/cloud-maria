package com.aibees.service.maria.multipart.controller.impl;

import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.multipart.service.impl.MgmtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/upload")
@AllArgsConstructor
public class UploadController {

    private final MgmtService mgmtService;
    @PostMapping("/files")
    public ResponseEntity<ResponseData> uploadImageFiles(@RequestParam(value = "files") List<MultipartFile> files) {

        for(MultipartFile file : files) {
            System.out.println(file.getOriginalFilename());
            System.out.println(file.getSize());
        }

        mgmtService.convertFile(files);
        return ResponseEntity.ok(null);
    }
}

package com.aibees.service.maria.multipart.controller.impl;

import com.aibees.service.maria.multipart.controller.FileController;
import com.aibees.service.maria.multipart.service.impl.CompressedFileService;
import com.aibees.service.maria.multipart.dao.vo.ResourceVo;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aibees.service.maria.common.HttpUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@AllArgsConstructor
@RequestMapping("/compress")
public class CompressedController implements FileController {

    private final HttpUtils httpUtils;
    private final CompressedFileService compressService;

    @Override
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> downloadResource(String filename) {
        // resource 데이터를 뽑아온다.
        ResourceVo resData = compressService.getResource("", filename);

        try {
            InputStreamResource resource = new InputStreamResource( new FileInputStream (
                    resData.getPath() + resData.getFilename()
            ));

            return ResponseEntity.ok()
                    .headers(httpUtils.getDownloadHeader(resData))
                    .contentLength(resData.getFile().length())
                    .body(resource);
        } catch(FileNotFoundException fne) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    @PostMapping("")
    public ResponseEntity<Void> uploadResource(MultipartFile file, String data) {
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/uuid")
    public ResponseEntity<String> getUUIDTest() {
        String uuid = compressService.getUUID("ZIP");
        return ResponseEntity.ok().body(uuid);
    }
}

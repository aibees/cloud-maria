package com.aibees.service.maria.multipart.controller.impl;

import com.aibees.service.maria.common.CONSTANT;
import com.aibees.service.maria.multipart.controller.FileController;
import com.aibees.service.maria.multipart.dao.obj.ImageFileCondition;
import com.aibees.service.maria.multipart.dao.vo.ImageFileVo;
import com.aibees.service.maria.multipart.service.impl.ImageFileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/image")
public class ImageController implements FileController {

    private final ImageFileService imageFileService;

    @GetMapping({ "/list/{category}", "/list/{category}/{ym}" })
    public ResponseEntity<List<ImageFileVo>> getImageListByCondition(ImageFileCondition params) {

        params.setCategory(params.getCategory().toUpperCase());

        return ResponseEntity.ok().body(
            imageFileService.getImageListByCondition(
                    ImageFileCondition
                            .builder()
                            .category(params.getCategory())
                            .ym(params.getYm())
                            .build()
            )
        );
    }

    @GetMapping("/{category}/{ym}/{filename}")
    public ResponseEntity<Resource> displayImageResource(ImageFileCondition params) {
        String base_dir = CONSTANT.FILE_HOME.concat(CONSTANT.IMAGE_HOME);

        System.out.println("base_dir : " + base_dir);
        System.out.println(params.toString());

        return null;
    }

    @GetMapping(value="/imagetest/{filename}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getImageTest(ImageFileCondition params) {

        try {
            String inputFile = "D:\\".concat(params.getFilename());
            System.out.println(inputFile);
            Path path = new File(inputFile).toPath();

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                    .body(new FileSystemResource(path));
        } catch(IOException e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    public ResponseEntity<Resource> downloadResource(@RequestParam String res) {
        return null;
    }

    @Override
    public ResponseEntity<Void> uploadResource(@RequestParam MultipartFile file, @RequestParam String data) {
        return null;
    }
}

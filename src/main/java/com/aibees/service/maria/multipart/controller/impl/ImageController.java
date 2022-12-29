package com.aibees.service.maria.multipart.controller.impl;

import com.aibees.service.maria.common.CONSTANT;
import com.aibees.service.maria.multipart.controller.FileController;
import com.aibees.service.maria.multipart.dao.obj.ImageFileCondition;
import com.aibees.service.maria.multipart.dao.vo.ImageFileVo;
import com.aibees.service.maria.multipart.service.impl.ImageFileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
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

    @Override
    public ResponseEntity<Resource> downloadResource(@RequestParam String res) {
        return null;
    }

    @Override
    public ResponseEntity<Void> uploadResource(@RequestParam MultipartFile file, @RequestParam String data) {
        return null;
    }
}

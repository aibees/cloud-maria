package com.aibees.service.maria.multipart.controller.impl;

import com.aibees.service.maria.multipart.domain.vo.FileVo;
import com.aibees.service.maria.multipart.service.impl.CommonFileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class CommonController {

    private final CommonFileService commonFileService;

    @GetMapping("/list")
    public List<FileVo> getChildFiles(@RequestParam(value="fileId") String fileId) {

        return null;
    }

    @PostMapping("/folder")
    public FileVo createDir(@RequestBody FileVo vo) {
        return commonFileService.createDirectory(vo);
    }
}

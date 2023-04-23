package com.aibees.service.maria.multipart.controller.impl;

import com.aibees.service.maria.multipart.domain.vo.FileVo;
import com.aibees.service.maria.multipart.service.impl.CommonFileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 파일 조회, 파일 및 폴더 생성/삭제 등 공통처리모듈
 */
@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class CommonController {

    private final CommonFileService commonFileService;

    @GetMapping("/list")
    public List<FileVo> getChildFiles(@RequestParam(value="fileId", defaultValue = "0") String fileId) {
        return commonFileService.getResourceList(fileId);
    }

    @GetMapping("/{fileId}")
    public FileVo getFileInfoDetail(@PathVariable String fileId) {
        return commonFileService.getFileInfo(fileId);
    }

    @PostMapping("/folder")
    public FileVo createDir(@RequestBody FileVo vo) {
        return commonFileService.createDirectory(vo);
    }


}

package com.aibees.service.maria.multipart.controller.impl;

import com.aibees.service.maria.common.vo.ResponseData;
import com.aibees.service.maria.multipart.domain.dto.CommonFileCondition;
import com.aibees.service.maria.multipart.domain.dto.FileCondition;
import com.aibees.service.maria.multipart.domain.vo.FileVo;
import com.aibees.service.maria.multipart.service.impl.CommonFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 파일 조회, 파일 및 폴더 생성/삭제 등 공통처리모듈
 */
@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class CommonController {

    private final CommonFileService commonFileService;

    /**
     * 해당파일 child 리스트를 조회
     * @param fileId
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseData> getChildFiles(@RequestParam(name = "fileId") Long fileId) {
        return commonFileService.getResourceList(fileId);
    }

    /**
     * 파일 metadata 조회
     * @param fileId
     * @return
     */
    @GetMapping("/detail")
    public FileVo getFileInfoDetail(@RequestParam String fileId) {
        return commonFileService.getFileInfo(fileId);
    }

    /**
     * 신규 folder 생성
     * @param vo
     * @return
     */
    @PostMapping("/folder")
    public ResponseEntity<ResponseData> createDir(@RequestBody FileVo vo) {
        System.out.println(vo.toString());
        return commonFileService.createDirectory(vo);
    }


}

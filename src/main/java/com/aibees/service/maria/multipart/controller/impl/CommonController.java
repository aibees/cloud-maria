package com.aibees.service.maria.multipart.controller.impl;

import com.aibees.service.maria.multipart.domain.vo.FileVo;
import com.aibees.service.maria.multipart.service.impl.CommonFileService;
import lombok.AllArgsConstructor;
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
    public List<FileVo> getChildFiles(@RequestParam(value="fileId", defaultValue = "0") String fileId) {
        return commonFileService.getResourceList(fileId);
    }

    /**
     * 상위 폴더로 올라간 후 상위폴더의 child 리스트 조회
     * @param fileId
     * @return
     */
    @PostMapping("/back")
    public Map<String, Object> getParentWithLists(@RequestParam(value="fileId") String fileId) {
        return commonFileService.getParentFileList(fileId);
    }

    /**
     * 파일 metadata 조회
     * @param fileId
     * @return
     */
    @GetMapping("/{fileId}")
    public FileVo getFileInfoDetail(@PathVariable String fileId) {
        return commonFileService.getFileInfo(fileId);
    }

    /**
     * 신규 folder 생성
     * @param vo
     * @return
     */
    @PostMapping("/folder")
    public FileVo createDir(@RequestBody FileVo vo) {
        return commonFileService.createDirectory(vo);
    }


}

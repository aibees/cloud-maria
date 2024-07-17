package com.aibees.service.maria.multipart.controller.impl;

import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.multipart.domain.dto.PageableDto;
import com.aibees.service.maria.multipart.service.impl.MgmtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/mgmt")
public class MgmtController {

    private final MgmtService mgmtService;

    @GetMapping("/list")
    public ResponseEntity<ResponseData> getFileMgmtList(PageableDto page) {
        return mgmtService.getFileImageMgmt(page);
    }
}

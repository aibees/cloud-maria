package com.aibees.service.maria.account.controller;

import com.aibees.service.maria.account.domain.dto.system.CategoryReq;
import com.aibees.service.maria.account.domain.dto.system.PresetMasterReq;
import com.aibees.service.maria.account.service.system.SystemCategoryService;
import com.aibees.service.maria.account.service.system.SystemPresetService;
import com.aibees.service.maria.account.service.system.SystemSourceService;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/system")
@Slf4j
public class SystemController {

    private final SystemPresetService presetService;
    private final SystemSourceService sourceService;
    private final SystemCategoryService categoryService;

    @GetMapping("/preset/headers")
    public ResponseEntity<ResponseData> getPresetHeaderList(@RequestParam PresetMasterReq param) {
        log.info(param.toString());
        return presetService.getPresetMasterList(param);
    }

    @GetMapping("/preset/headers/popup")
    public ResponseEntity<ResponseData> getPresetHeaderListforPopup(@RequestParam(required = false) PresetMasterReq param) {
        PresetMasterReq tmp = new PresetMasterReq();
        if (Objects.isNull(param)) {
            tmp.setSearchType("popup");
            param = tmp;
        }
        log.info(param.toString());
        return presetService.getPresetMasterList(param);
    }

    @GetMapping("/sources")
    public ResponseEntity<ResponseData> getSourceList() {
        return sourceService.getServiceCodeList();
    }

    @GetMapping("/categories")
    public ResponseEntity<ResponseData> getCategoryListBySource(CategoryReq param) {
        log.info(param.toString());
        return categoryService.getCategoryBySourceCd(param);
    }
}

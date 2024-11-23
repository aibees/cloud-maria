package com.aibees.service.maria.account.controller;

import com.aibees.service.maria.account.domain.dto.system.*;
import com.aibees.service.maria.account.service.system.SystemCategoryService;
import com.aibees.service.maria.account.service.system.SystemPresetService;
import com.aibees.service.maria.account.service.system.SystemSourceService;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
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
    public List<PresetMasterRes> getPresetHeaderList(@RequestParam PresetMasterReq param) {
        return presetService.getPresetMasterList(param);
    }

    @GetMapping("/preset/headers/popup")
    public List<PresetMasterRes> getPresetHeaderListforPopup(@RequestParam(required = false) PresetMasterReq param) {
        PresetMasterReq tmp = new PresetMasterReq();
        if (Objects.isNull(param)) {
            tmp.setSearchType("popup");
            param = tmp;
        }
        return presetService.getPresetMasterList(param);
    }

    @GetMapping("/sources")
    public List<SourceRes> getSourceList() {
        return sourceService.getServiceCodeList();
    }

    @GetMapping("/categories")
    public List<CategoryRes> getCategoryListBySource(CategoryReq param) {
        return categoryService.getCategoryBySourceCd(param);
    }
}

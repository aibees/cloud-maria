package com.aibees.service.maria.account.controller;

import com.aibees.service.maria.account.domain.dto.account.AccountSettingReq;
import com.aibees.service.maria.account.service.account.SettingService;
import com.aibees.service.maria.common.vo.ResponseData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/master")
@Slf4j
public class MasterController {

    private final SettingService settingService;

    @PostMapping("/detail")
    public ResponseEntity<ResponseData> getSettingResource(@RequestBody AccountSettingReq param) {
        log.info(param.toString());
        return settingService.getSettingListForCommon(param);
    }
}

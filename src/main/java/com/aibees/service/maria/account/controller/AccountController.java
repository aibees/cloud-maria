package com.aibees.service.maria.account.controller;

import com.aibees.service.maria.account.facade.AccountFacade;
import com.aibees.service.maria.common.vo.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/account/common")
public class AccountController {

    private final AccountFacade accountFacade;

    @GetMapping("/import/list")
    public ResponseEntity<ResponseData> getFileHashList(@RequestParam String type) {
        return accountFacade.getImportFileNameList(type);
    }
}

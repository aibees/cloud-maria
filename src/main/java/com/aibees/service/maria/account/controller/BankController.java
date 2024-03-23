package com.aibees.service.maria.account.controller;

import com.aibees.service.maria.account.domain.dto.bank.BankStatementReq;
import com.aibees.service.maria.common.vo.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account/bank")
public class BankController {

    /**
     * Bank Statement 리스트 조회
     * @param param
     * @return
     */
    @GetMapping("/statement/list")
    public ResponseEntity<ResponseData> getBankStatementList(@RequestParam BankStatementReq param) {
        return null;
    }
}

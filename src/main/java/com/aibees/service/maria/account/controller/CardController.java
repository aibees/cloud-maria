package com.aibees.service.maria.account.controller;

import com.aibees.service.maria.common.vo.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/account/card")
public class CardController {

    /**
     * Card Statement 리스트 조회
     * @param param
     * @return
     */
    @GetMapping("/statement/list")
    public ResponseEntity<ResponseData> getCardStatementList() {
        return null;
    }
}

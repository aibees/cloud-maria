package com.aibees.service.maria.accountbook.controller;

import com.aibees.service.maria.accountbook.entity.dto.CardDto;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.accountbook.service.AccountService;
import com.aibees.service.maria.accountbook.service.CardAccountService;
import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.common.vo.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/card")
public class CardController {

    private final CardAccountService cardService;

    @GetMapping("/list")
    public ResponseEntity<ResponseData> getCardStatementList(@RequestParam CardDto param) {
        return cardService.getCardStatementList(param);
    }
}

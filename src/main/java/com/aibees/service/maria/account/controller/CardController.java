package com.aibees.service.maria.account.controller;

import com.aibees.service.maria.account.domain.dto.card.CardInfoReq;
import com.aibees.service.maria.account.domain.dto.card.CardInfoRes;
import com.aibees.service.maria.account.domain.dto.card.CardStatementReq;
import com.aibees.service.maria.account.domain.dto.card.CardStatementRes;
import com.aibees.service.maria.account.service.card.CardService;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/account/card")
public class CardController {

    private final CardService cardService;

    /**
     * Card Statement 리스트 조회
     * @param
     * @return
     */
    @GetMapping("/statement/list")
    public List<CardStatementRes> getCardStatementList(CardStatementReq param) {
        return cardService.getCardStatementList(param);
    }

    /***************************************************
     ********        CARD - Account Info        ********
     ***************************************************/
    @GetMapping("/infos") // CardInfo.vue - getCardInfoList
    public List<CardInfoRes> getCardInfoList(@RequestParam(required = false) CardInfoReq param) {
        return cardService.getCardInfoList(param);
    }

    @PostMapping("/infos") // CardInfo.vue - saveData
    public void saveCardInfoData(@RequestBody CardInfoReq param) {
        List<CardInfoReq> list = param.getCardInfoReqs();
        cardService.saveCardInfo(list);
    }
}

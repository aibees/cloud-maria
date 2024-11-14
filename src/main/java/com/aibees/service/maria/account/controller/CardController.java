package com.aibees.service.maria.account.controller;

import com.aibees.service.maria.account.domain.dto.card.CardInfoReq;
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
     * @param param
     * @return
     */
    @GetMapping("/statement/list")
    public ResponseEntity<ResponseData> getCardStatementList() {
        return null;
    }

    /***************************************************
     ********        CARD - Account Info        ********
     ***************************************************/
    @GetMapping("/infos") // CardInfo.vue - getCardInfoList
    public ResponseEntity<ResponseData> getCardInfoList(@RequestParam(required = false) CardInfoReq param) {
        return cardService.getCardInfoList(param);
    }

    @PostMapping("/infos") // CardInfo.vue - saveData
    public ResponseEntity<ResponseData> saveCardInfoData(@RequestBody CardInfoReq param) {
        List<CardInfoReq> list = param.getCardInfoReqs();
        for(CardInfoReq data : list) {
            System.out.println(data);
        }
        return cardService.saveCardInfo(list);
    }
}

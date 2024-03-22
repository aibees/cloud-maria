package com.aibees.service.maria.accountbook.service;

import com.aibees.service.maria.accountbook.domain.card.CardData;
import com.aibees.service.maria.accountbook.entity.dto.CardDto;
import com.aibees.service.maria.accountbook.entity.mapper.AccountCardMapper;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.common.vo.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CardAccountService extends CommonAccountService {

    private final AccountCardMapper accountCardMapper;

    public ResponseEntity<ResponseData> getCardStatementList(CardDto param) {
        CardData card = CardData.createForStatement(accountCardMapper);

        try {
            List<CardStatement> resultList = card.getCardStatementList(param);

            return successResponse(resultList);
        } catch (Exception e) {
            return failedResponse(e);
        }
    }
}

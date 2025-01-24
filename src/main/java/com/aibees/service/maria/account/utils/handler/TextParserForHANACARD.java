package com.aibees.service.maria.account.utils.handler;

import com.aibees.service.maria.account.domain.entity.account.ImportStatementTmp;
import com.aibees.service.maria.account.domain.entity.card.AccountCardInfo;
import com.aibees.service.maria.common.utils.DateUtils;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;


public class TextParserForHANACARD implements TextParser {
    @Override
    public Map<String, Object> process(String[] texts) {
        ImportStatementTmp statementParam;
        // 1. card info
        Map<String, Object> cardInfoParam = new HashMap<>();
        cardInfoParam.put("company", texts[0].substring(0, 2));
        cardInfoParam.put("cardNo", texts[0].substring(2, 6).replace("*", "%"));
        AccountCardInfo cardInfo = null; //infoMapper.selectCardInfoByCondition(cardInfoParam);

        // 2. amount
        String amount = texts[2].replace(",", "").replace("원", "");

        // 3. ymd & times
        String ymd = DateUtils.getTodayStr("yyyy") + texts[4].replace("/", "");
        String times = texts[5].replace(":", "").concat("00");

        // 4. remark
        String remark = texts[6];

        statementParam = ImportStatementTmp.builder()
                .ymd(ymd)
                .times(times)
                .amount(Long.parseLong(amount))
                .remark(remark)
                .cardNo(cardInfo.getCardNo())
                .build();

        return ImmutableMap.of("statement", statementParam);
    }
}

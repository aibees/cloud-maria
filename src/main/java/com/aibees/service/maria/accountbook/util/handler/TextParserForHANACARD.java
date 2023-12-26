package com.aibees.service.maria.accountbook.util.handler;

import com.aibees.service.maria.accountbook.entity.mapper.AccountCardInfoMapper;
import com.aibees.service.maria.accountbook.entity.vo.CardInfoStatement;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.common.DateUtils;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;


public class TextParserForHANACARD implements TextParser {

    private AccountCardInfoMapper infoMapper;

    public TextParserForHANACARD(AccountCardInfoMapper infoMapper) {
        this.infoMapper = infoMapper;
    }

    @Override
    public Map<String, Object> process(String[] texts) {
        CardStatement statementParam;
        // 1. card info
        Map<String, Object> cardInfoParam = new HashMap<>();
        cardInfoParam.put("company", texts[0].substring(0, 2));
        cardInfoParam.put("cardNo", texts[0].substring(2, 6).replace("*", "%"));
        CardInfoStatement cardInfo = infoMapper.selectCardInfoByCondition(cardInfoParam);

        // 2. amount
        String amount = texts[2].replace(",", "").replace("원", "");

        // 3. ymd & times
        String ymd = DateUtils.getTodayStr("yyyy") + texts[4].replace("/", "");
        String times = texts[5].replace(":", "").concat("00");

        // 4. remark
        String remark = texts[6];

        statementParam = CardStatement.builder()
                .ymd(ymd)
                .times(times)
                .amount(Long.parseLong(amount))
                .remark(remark)
                .cardNo(cardInfo.getCardNo())
                .cardNm(cardInfo.getCardName())
                .build();

        return ImmutableMap.of("statement", statementParam);
    }
}

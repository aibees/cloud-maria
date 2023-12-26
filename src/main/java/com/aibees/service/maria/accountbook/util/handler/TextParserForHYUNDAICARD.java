package com.aibees.service.maria.accountbook.util.handler;

import com.aibees.service.maria.accountbook.entity.mapper.AccountCardInfoMapper;
import com.aibees.service.maria.accountbook.entity.vo.CardInfoStatement;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.common.DateUtils;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TextParserForHYUNDAICARD implements TextParser {

    private AccountCardInfoMapper infoMapper;

    public TextParserForHYUNDAICARD(AccountCardInfoMapper infoMapper) {
        this.infoMapper = infoMapper;
    }

    @Override
    public Map<String, Object> process(String[] texts) {
        CardStatement statementParam;
        String[] splitOfCard = texts[0].split(AccConstant.SPACE_STR);

        Map<String, Object> cardInfoParam = new HashMap<>();
        cardInfoParam.put("cardName", splitOfCard[1]);
        cardInfoParam.put("company", splitOfCard[0].substring(0, 2));
        CardInfoStatement cardInfo = infoMapper.selectCardInfoByCondition(cardInfoParam);

        if(Objects.isNull(cardInfo)) {
            return null;
        }

        // 2. amount
        String amount = texts[2].split(AccConstant.SPACE_STR)[0].replace(",", "").replace("원", "");

        // 3. ymd & times
        String[] ymdTimes = texts[3].split(AccConstant.SPACE_STR);
        String ymd = DateUtils.getTodayStr("yyyy") + ymdTimes[0].replace("/", "");
        String times = ymdTimes[1].replace(":", "").concat("00");

        // 4. remark
        String remark = texts[4];

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

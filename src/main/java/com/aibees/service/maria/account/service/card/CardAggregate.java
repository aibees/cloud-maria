package com.aibees.service.maria.account.service.card;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CardAggregate {
//    private final CardService cardService;

//    /**
//     * 카드 별 이번 달 잔액
//     * @return List
//     */
//    public List<Map<String, Object>> getRemainAmountByCard() {
//        List<Map<String, Object>> cardRemainDataList = new ArrayList<>();
//        Map<String, Object> schParam = new HashMap<>();
//
//        try {
//            CardData cardData = CardData.createWithInfo(cardInfoMapper);
//            cardData.prepareInfoStatementByCondition(schParam);
//
//            cardData.getCardInfoStatements()
//                    .stream()
//                    .filter(card -> StringUtils.isEquals(AccConstant.YES, card.getSelectedMain()))
//                    .forEach(card -> {
//                        Map<String, Object> amtParam = new HashMap<>();
//                        Map<String, Object> remain = new HashMap<>();
//
//                        amtParam.put("ym", DateUtils.getTodayStr("yyyyMM"));
//                        amtParam.put("cardNo", card.getCardNo());
//                        Long usedAmount = accountCardMapper.selectUsedAmountByYm(amtParam);
//                        if (usedAmount == null) {
//                            usedAmount = 0L;
//                        }
//
//                        double percentage = ((double) usedAmount / card.getLimitAmt()) * 100;
//
//                        remain.put("cardNm", card.getCardName());
//                        remain.put("limitedAmt", card.getLimitAmt());
//                        remain.put("usedAmt", usedAmount);
//                        remain.put("usedPercentage", String.format("%.2f", percentage));
//                        remain.put("remainAmt", card.getLimitAmt() - usedAmount);
//
//                        cardRemainDataList.add(remain);
//                    });
//
//            return cardRemainDataList;
//        } catch(Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    /**
//     * 카드 건 별 결제내역 제출 -> card_statement_sms
//     * @param param
//     * @return
//     */
//    public Map<String, Object> registCardPaymentText(Map<String, Object> param) {
//        Map<String, Object> result = new HashMap<>();
//        boolean flag = true;
//
//        // 결제내역 텍스트 추출
//        String smsText = MapUtils.getString(param, "text");
//        String type = MapUtils.getString(param, "type");
//
//        try {
//            if(type.equals(IMPORT_CARD)) {
//                CardData card = CardData.createWithTransfer(type, AccConstant.TRX_SMS, accountCardMapper, cardInfoMapper);
//                card.textParse(type, smsText);
//                flag = card.transferData();
//
//            } else if(type.equals(IMPORT_BANK)) {
//                BankData bank = BankData.createWithTransfer(type, AccConstant.TRX_SMS, accountBankMapper, bankInfoMapper);
//                bank.textParse(type, smsText);
//                flag = bank.transferData();
//
//            }
//
//            if(!flag) {
//                throw new Exception("SMS Text Registration Failed");
//            }
//        } catch (Exception e) {
//            result.put(AccConstant.CM_RESULT, AccConstant.CM_FAILED);
//            result.put("message", e.getMessage());
//        }
//        return ImmutableMap.of(AccConstant.CM_RESULT, AccConstant.CM_SUCCESS, AccConstant.CM_DATA, result);
//    }

//    /**
//     * 최근 신용카드 사용내역 (recently 5)
//     */
//    public List<Map<String, Object>> getRecentUseCardStatementList() {
//        return accountCardMapper.selectRecentCardStatement();
//    }
}

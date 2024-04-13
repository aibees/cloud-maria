package com.aibees.service.maria.account.service.bank;

public class BankCloseService {

    public void closeAccountByMonth() {

    }

//    /**
//     * 월 별 마감데이터 조회하기
//     * @param type
//     * @param ym
//     * @return
//     */
//    public Map<String, Object> closeDataList(String type, String ym) {
//        List<Map<String, Object>> result = new ArrayList<>();
//        String queryType = "closeDataList";
//        try {
//            if(type.equals(IMPORT_CARD)) {
//                CardData card = null;
//            } else {
//                // 은행 마감데이터 생성
//                BankData bank = BankData.createWithClose(bankCloseMapper, bankInfoMapper);
//
//                // 은행정보 전체조회
//                bank.prepareInfoStatementByCondition(ImmutableMap.of());
//                List<BankInfoStatement> bankList = bank.getBankInfoStatements();
//
//                // 은행 별로 마감조회데이터 가져오기
//                bankList.forEach(b -> {
//                    System.out.println(b.toString());
//
//                    // 바깥 쿼리에서 같이 사용할 파라미터
//                    Map<String, Object> param = ImmutableMap.of("bankId", b.getBankId(), "ym", ym);
//
//                    // statement 쿼리에서 사용할 파라미터
//                    Map<String, Object> queryParam = ImmutableMap.of(
//                            "type", queryType,
//                            "param", param
//                    );
//                    try {
//                        // 전월 마감라인 가져오기
//                        BankCloseStatement closeState = bank.getCloseMapper().getBankCloseByBankidAndYm(param);
//                        if(closeState == null)
//                            closeState = new BankCloseStatement();
//
//                        Map<String, Object> bankData = new HashMap<>();
//                        bankData.put("bankId", b.getBankId());
//                        bankData.put("bankNm", b.getBankNm());
//                        bankData.put("limitAmt", b.getLimitAmt());
//                        bankData.put("lastAmt", closeState.getLastAmount());
//                        bankData.put("ym", ym);
//                        bankData.put("closeColor", "#7a7aa6");
//                        bankData.put("completeFlag", closeState.getCurrCloseYn());
//                        bankData.put("completeBtn", (StringUtils.isEquals(closeState.getCurrCloseYn(), AccConstant.YES))? "확정완료" : "확정하기");
//
//                        // 은행 별 월 마감데이터 조회하기
//                        bank.prepareCloseDataByCondition(queryParam);
//                        List<Map<String, Object>> lineData = bank.getCloseDataList();
//
//                        if(Objects.nonNull(lineData) && lineData.size() > 0) {
//                            bankData.put("lineData", lineData);
//                            summaryData(bankData, lineData);
//                        } else {
//                            bankData.put("lineData", null);
//                        }
//
//                        result.add(bankData);
//                    } catch(Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//
//            return ImmutableMap.of(
//                    AccConstant.CM_RESULT, AccConstant.CM_SUCCESS,
//                    AccConstant.CM_DATA, result
//            );
//        } catch(Exception e) {
//            return ImmutableMap.of(
//                    AccConstant.CM_RESULT, AccConstant.CM_FAILED,
//                    AccConstant.CM_MESSAGE, e.getMessage()
//            );
//        }
//    }
}

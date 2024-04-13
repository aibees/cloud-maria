package com.aibees.service.maria.account.service.card;

import org.springframework.stereotype.Service;

@Service
public class CardClose {
//    public ResponseEntity<ResponseData> confirmCloseData(Map<String, Object> param) {
//        String type = MapUtils.getString(param, "type");
//        try {
//            if (type.equals(AccConstant.IMPORT_CARD)) {
//
//            }else {
//                Map<String, Object> paramData = (Map<String, Object>)param.get("data");
//                String curYm = MapUtils.getString(paramData, "ym");
//                String nextYm = DateUtils.addMonthDate(curYm+"01", "yyyyMMdd", 1);
//                paramData.put("nextYm", nextYm);
//                BankData bank = BankData.createWithClose(bankCloseMapper, bankInfoMapper);
//                bank.setBankCloseStatementWithMap(paramData);
//
//                if(bank.getCloseMapper().insertBankCloseData(bank.getBankCloseStatement()) < 1) {
//                    throw new Exception("Abnormal INSERT - confirmCloseData");
//                }
//            }
//            return ResponseEntity
//                    .ok(ResponseData
//                            .builder()
//                            .status(HttpStatus.OK)
//                            .build());
//        } catch(Exception e) {
//            e.printStackTrace();
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(ResponseData.builder().message(e.getMessage()).build());
//        }
//    }
//
//    public Map<String, Object> getDetailDataListForCheck(Map<String, Object> param) {
//        String type = MapUtils.getString(param, "type");
//        String queryType = "closeDetailList";
//        try {
//            List<Map<String, Object>> result = new ArrayList<>();
//            if (type.equals(IMPORT_CARD)) {
//
//            } else {
//                BankData bank = BankData.createWithClose(bankCloseMapper, bankInfoMapper);
//                Map<String, Object> queryParam = ImmutableMap.of(
//                        "type", queryType,
//                        "param", param
//                );
//                bank.prepareCloseDataByCondition(queryParam);
//                result = bank.getCloseDataList();
//            }
//
//            return ImmutableMap.of(
//                    AccConstant.CM_RESULT, AccConstant.CM_SUCCESS,
//                    AccConstant.CM_DATA, result
//            );
//        } catch (Exception e) {
//            return ImmutableMap.of(
//                    AccConstant.CM_RESULT, AccConstant.CM_FAILED,
//                    AccConstant.CM_MESSAGE, e.getMessage()
//            );
//        }
//    }
//
//    public Map<String, Object> saveCloseDetail(Map<String, Object> param) {
//        String type = MapUtils.getString(param, "type");
//        String data = AccConstant.EMPTY_STR;
//        List<Map<String, Object>> paramData = (List<Map<String, Object>>)param.get("data");
//        try {
//            if (type.equals(IMPORT_BANK)) {
//                BankData bank = BankData.createWithClose(bankCloseMapper, bankInfoMapper);
//                bank.setBankStatementsWithMap(paramData);
//
//                int dataSize = bank.getBankStatements().size();
//                AtomicInteger uptSize = new AtomicInteger(0);
//                bank.getBankStatements().forEach(state -> {
//                    uptSize.addAndGet(bank.getCloseMapper().updateBankStatementStatus(state));
//                });
//
//                if(dataSize > uptSize.get()) {
//                    throw new Exception("업데이트 오류 발생");
//                } else {
//                    data = "성공";
//                }
//            } else {
//
//            }
//
//            return ImmutableMap.of(
//                    AccConstant.CM_RESULT, AccConstant.CM_SUCCESS,
//                    AccConstant.CM_DATA, data
//            );
//        } catch(Exception e) {
//            return ImmutableMap.of(
//                    AccConstant.CM_RESULT, AccConstant.CM_FAILED,
//                    AccConstant.CM_MESSAGE, e.getMessage()
//            );
//        }
//    }
//
//    /*************************
//     **** private  method ****
//     *************************/
//
//    /**
//     *
//     * @param bankData
//     * @param lineData
//     */
//    private void summaryData(Map<String, Object> bankData, List<Map<String, Object>> lineData) {
//        AtomicLong profitAmt = new AtomicLong(0L);
//        List<Map<String, Object>> profitData = new ArrayList<>();
//        AtomicLong lossAmt = new AtomicLong(0L);
//        List<Map<String, Object>> lossData = new ArrayList<>();
//
//        lineData.forEach(line -> {
//            int dataCnt = MapUtils.getInteger(line, "count");
//            int confirmCnt = MapUtils.getInteger(line, "confirmCnt");
//
//            if(dataCnt == confirmCnt) {
//                line.put("confirmMsg", "검토완료");
//                line.put("confirmCode", "checkComplete");
//            } else {
//                line.put("confirmMsg", "검토하기");
//                line.put("confirmCode", "");
//            }
//
//            String entry_cd = MapUtils.getString(line, "entryCd");
//            if(entry_cd.equals("0")) {
//                // 수입
//                profitAmt.addAndGet(MapUtils.getLong(line, "amount"));
//                profitData.add(line);
//            } else {
//                // 지출
//                lossAmt.addAndGet(MapUtils.getLong(line, "amount"));
//                lossData.add(line);
//            }
//        });
//
//        bankData.put("profitAcc", profitAmt);
//        bankData.put("profitData", profitData);
//        bankData.put("profitSize", profitData.size());
//        bankData.put("lossAcc", lossAmt);
//        bankData.put("lossData", lossData);
//        bankData.put("lossSize", lossData.size());
//        bankData.put("resultAcc", profitAmt.longValue() - lossAmt.longValue());
//        bankData.put("curAmt", MapUtils.getLong(bankData, "lastAmt") + (profitAmt.longValue() - lossAmt.longValue()));
//    }

}

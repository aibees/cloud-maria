package com.aibees.service.maria.accountbook.service;

import com.aibees.service.maria.accountbook.domain.bank.BankData;
import com.aibees.service.maria.accountbook.domain.card.CardData;
import com.aibees.service.maria.accountbook.entity.mapper.AccountBankCloseMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountBankInfoMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountCardInfoMapper;
import com.aibees.service.maria.accountbook.entity.vo.BankCloseStatement;
import com.aibees.service.maria.accountbook.entity.vo.BankInfoStatement;
import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.common.DateUtils;
import com.aibees.service.maria.common.MapUtils;
import com.aibees.service.maria.common.StringUtils;
import com.aibees.service.maria.common.vo.ResponseData;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.aibees.service.maria.accountbook.util.AccConstant.IMPORT_BANK;
import static com.aibees.service.maria.accountbook.util.AccConstant.IMPORT_CARD;

@Service
@AllArgsConstructor
public class CloseService {

    private final AccountBankCloseMapper bankCloseMapper;
    private final AccountBankInfoMapper bankInfoMapper;
    private final AccountCardInfoMapper cardInfoMapper;

    /**
     * 월 별 마감데이터 조회하기
     * @param type
     * @param ym
     * @return
     */
    public Map<String, Object> closeDataList(String type, String ym) {
        List<Map<String, Object>> result = new ArrayList<>();
        String queryType = "closeDataList";
        try {
            if(type.equals(IMPORT_CARD)) {
                CardData card = null;
            } else {
                // 은행 마감데이터 생성
                BankData bank = BankData.createWithClose(bankCloseMapper, bankInfoMapper);

                // 은행정보 전체조회
                bank.prepareInfoStatementByCondition(ImmutableMap.of());
                List<BankInfoStatement> bankList = bank.getBankInfoStatements();

                // 은행 별로 마감조회데이터 가져오기
                bankList.forEach(b -> {
                    System.out.println(b.toString());

                    // 바깥 쿼리에서 같이 사용할 파라미터
                    Map<String, Object> param = ImmutableMap.of("bankId", b.getBankId(), "ym", ym);

                    // statement 쿼리에서 사용할 파라미터
                    Map<String, Object> queryParam = ImmutableMap.of(
                            "type", queryType,
                            "param", param
                    );
                    try {
                        // 전월 마감라인 가져오기
                        BankCloseStatement closeState = bank.getCloseMapper().getBankCloseByBankidAndYm(param);
                        if(closeState == null)
                            closeState = new BankCloseStatement();

                        Map<String, Object> bankData = new HashMap<>();
                        bankData.put("bankId", b.getBankId());
                        bankData.put("bankNm", b.getBankNm());
                        bankData.put("limitAmt", b.getLimitAmt());
                        bankData.put("lastAmt", closeState.getLastAmount());
                        bankData.put("ym", ym);
                        bankData.put("closeColor", "#7a7aa6");
                        bankData.put("completeFlag", closeState.getCurrCloseYn());
                        bankData.put("completeBtn", (StringUtils.isEquals(closeState.getCurrCloseYn(), AccConstant.YES))? "확정완료" : "확정하기");

                        // 은행 별 월 마감데이터 조회하기
                        bank.prepareCloseDataByCondition(queryParam);
                        List<Map<String, Object>> lineData = bank.getCloseDataList();

                        if(Objects.nonNull(lineData) && lineData.size() > 0) {
                            bankData.put("lineData", lineData);
                            summaryData(bankData, lineData);
                        } else {
                            bankData.put("lineData", null);
                        }

                        result.add(bankData);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_SUCCESS,
                    AccConstant.CM_DATA, result
            );
        } catch(Exception e) {
            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_FAILED,
                    AccConstant.CM_MESSAGE, e.getMessage()
            );
        }
    }

    public ResponseEntity<ResponseData> confirmCloseData(Map<String, Object> param) {
        String type = MapUtils.getString(param, "type");
        try {
            if (type.equals(AccConstant.IMPORT_CARD)) {

            }else {
                Map<String, Object> paramData = (Map<String, Object>)param.get("data");
                String curYm = MapUtils.getString(paramData, "ym");
                String nextYm = DateUtils.addMonthDate(curYm+"01", "yyyyMMdd", 1);
                paramData.put("nextYm", nextYm);
                BankData bank = BankData.createWithClose(bankCloseMapper, bankInfoMapper);
                bank.setBankCloseStatementWithMap(paramData);

                if(bank.getCloseMapper().insertBankCloseData(bank.getBankCloseStatement()) < 1) {
                    throw new Exception("Abnormal INSERT - confirmCloseData");
                }
            }
            return ResponseEntity
                    .ok(ResponseData
                            .builder()
                            .status(HttpStatus.OK)
                            .build());
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseData.builder().message(e.getMessage()).build());
        }
    }

    public Map<String, Object> getDetailDataListForCheck(Map<String, Object> param) {
        String type = MapUtils.getString(param, "type");
        String queryType = "closeDetailList";
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            if (type.equals(IMPORT_CARD)) {

            } else {
                BankData bank = BankData.createWithClose(bankCloseMapper, bankInfoMapper);
                Map<String, Object> queryParam = ImmutableMap.of(
                        "type", queryType,
                        "param", param
                );
                bank.prepareCloseDataByCondition(queryParam);
                result = bank.getCloseDataList();
            }

            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_SUCCESS,
                    AccConstant.CM_DATA, result
            );
        } catch (Exception e) {
            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_FAILED,
                    AccConstant.CM_MESSAGE, e.getMessage()
            );
        }
    }

    public Map<String, Object> saveCloseDetail(Map<String, Object> param) {
        String type = MapUtils.getString(param, "type");
        String data = AccConstant.EMPTY_STR;
        List<Map<String, Object>> paramData = (List<Map<String, Object>>)param.get("data");
        try {
            if (type.equals(IMPORT_BANK)) {
                BankData bank = BankData.createWithClose(bankCloseMapper, bankInfoMapper);
                bank.setBankStatementsWithMap(paramData);
                
                int dataSize = bank.getBankStatements().size();
                AtomicInteger uptSize = new AtomicInteger(0);
                bank.getBankStatements().forEach(state -> {
                    uptSize.addAndGet(bank.getCloseMapper().updateBankStatementStatus(state));
                });
                
                if(dataSize > uptSize.get()) {
                    throw new Exception("업데이트 오류 발생");
                } else {
                    data = "성공";
                }
            } else {

            }

            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_SUCCESS,
                    AccConstant.CM_DATA, data
            );
        } catch(Exception e) {
            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_FAILED,
                    AccConstant.CM_MESSAGE, e.getMessage()
            );
        }
    }

    /*************************
     **** private  method ****
     *************************/

    /**
     *
     * @param bankData
     * @param lineData
     */
    private void summaryData(Map<String, Object> bankData, List<Map<String, Object>> lineData) {
        AtomicLong profitAmt = new AtomicLong(0L);
        List<Map<String, Object>> profitData = new ArrayList<>();
        AtomicLong lossAmt = new AtomicLong(0L);
        List<Map<String, Object>> lossData = new ArrayList<>();

        lineData.forEach(line -> {
            int dataCnt = MapUtils.getInteger(line, "count");
            int confirmCnt = MapUtils.getInteger(line, "confirmCnt");

            if(dataCnt == confirmCnt) {
                line.put("confirmMsg", "검토완료");
                line.put("confirmCode", "checkComplete");
            } else {
                line.put("confirmMsg", "검토하기");
                line.put("confirmCode", "");
            }

            String entry_cd = MapUtils.getString(line, "entryCd");
            if(entry_cd.equals("0")) {
                // 수입
                profitAmt.addAndGet(MapUtils.getLong(line, "amount"));
                profitData.add(line);
            } else {
                // 지출
                lossAmt.addAndGet(MapUtils.getLong(line, "amount"));
                lossData.add(line);
            }
        });

        bankData.put("profitAcc", profitAmt);
        bankData.put("profitData", profitData);
        bankData.put("profitSize", profitData.size());
        bankData.put("lossAcc", lossAmt);
        bankData.put("lossData", lossData);
        bankData.put("lossSize", lossData.size());
        bankData.put("resultAcc", profitAmt.longValue() - lossAmt.longValue());
        bankData.put("curAmt", MapUtils.getLong(bankData, "lastAmt") + (profitAmt.longValue() - lossAmt.longValue()));
    }
}

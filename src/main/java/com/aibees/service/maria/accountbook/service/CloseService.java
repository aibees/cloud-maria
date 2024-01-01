package com.aibees.service.maria.accountbook.service;

import com.aibees.service.maria.accountbook.domain.bank.BankData;
import com.aibees.service.maria.accountbook.domain.card.CardData;
import com.aibees.service.maria.accountbook.entity.mapper.AccountBankCloseMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountBankInfoMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountCardInfoMapper;
import com.aibees.service.maria.accountbook.entity.vo.BankCloseStatement;
import com.aibees.service.maria.accountbook.entity.vo.BankInfoStatement;
import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.common.MapUtils;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.aibees.service.maria.accountbook.util.AccConstant.IMPORT_CARD;

@Service
@AllArgsConstructor
public class CloseService {

    private final AccountBankCloseMapper bankCloseMapper;
    private final AccountBankInfoMapper bankInfoMapper;
    private final AccountCardInfoMapper cardInfoMapper;

    public Map<String, Object> closeDataList(String type, String ym) {
        List<Map<String, Object>> result = new ArrayList<>();
        String queryType = "closeDataList";
        try {
            if(type.equals(IMPORT_CARD)) {
                CardData card = null;
            } else {
                BankData bank = BankData.createWithClose(bankCloseMapper, bankInfoMapper);
                bank.prepareInfoStatementByCondition(ImmutableMap.of()); // 은행 전체조회
                List<BankInfoStatement> bankList = bank.getBankInfoStatements();

                // 은행 별로 마감조회데이터 가져오기
                bankList.forEach(b -> {
                    System.out.println(b.toString());
                    Map<String, Object> param = ImmutableMap.of("bankId", b.getBankId(), "ym", ym);
                    Map<String, Object> queryParam = ImmutableMap.of(
                            "type", queryType,
                            "param", param
                    );
                    try {
                        BankCloseStatement closeState = bank.getCloseMapper().getBankCloseByBankidAndYm(queryParam);
                        if(closeState == null)
                            closeState = new BankCloseStatement();
                        Map<String, Object> bankData = new HashMap<>();
                        bankData.put("bankId", b.getBankId());
                        bankData.put("bankNm", b.getBankNm());
                        bankData.put("limitAmt", b.getLimitAmt());
                        bankData.put("lastAmt", closeState.getLastAmount());

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

package com.aibees.service.maria.accountbook.service;

import com.aibees.service.maria.accountbook.domain.bank.BankData;
import com.aibees.service.maria.accountbook.domain.card.CardData;
import com.aibees.service.maria.accountbook.entity.mapper.AccountBankInfoMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountBankMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountCardInfoMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountCardMapper;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.common.DateUtils;
import com.aibees.service.maria.common.MapUtils;
import com.aibees.service.maria.common.StringUtils;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

import static com.aibees.service.maria.accountbook.util.AccConstant.IMPORT_BANK;
import static com.aibees.service.maria.accountbook.util.AccConstant.IMPORT_CARD;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountBankMapper accountBankMapper;
    private final AccountCardMapper accountCardMapper;
    private final AccountBankInfoMapper bankInfoMapper;
    private final AccountCardInfoMapper cardInfoMapper;



    public List<Map<String, Object>> getCardInfoForOption() {
        List<Map<String, Object>> optionList = accountCardMapper.selectCardInfoForOption();
        optionList.add(0, ImmutableMap.of("card_no", "-1", "card_name", "전체"));
        return optionList;
    }

    /**
     * card Statement List 조회
     * @param cardParam
     * @return
     */
    public List<CardStatement> getCardStatementList(Map<String, Object> cardParam) {

        // handling parameter
        // 1. usage Type
        if(StringUtils.isEquals(MapUtils.getString(cardParam, "usage"), "-1")) {
            cardParam.put("usage", AccConstant.EMPTY_STR); // 전체 Usage 값 들어오면 empty로 변경
        }
        // 2. cardNo
        if(StringUtils.isEquals(MapUtils.getString(cardParam, "cardNo"), "-1")) {
            cardParam.put("cardNo", AccConstant.EMPTY_STR); // 전체 cardNo 값 들어오면 empty로 변경
        }
        return accountCardMapper.selectCardStatementList(cardParam);
    }

    /**
     * 엑셀 업로드 파일 파싱 후 Insert, 재조회
     * @param
     * @param
     */
    @Transactional
    public Map<String, Object> excelParse(String type, MultipartFile file) {
        try {
            String fileHash = "";

            if(type.endsWith(IMPORT_CARD)) {
                CardData card = CardData.createWithTransfer(type, AccConstant.TRX_EXCEL, accountCardMapper, null);
                card.excelParse(file);
                card.transferData();
                fileHash = card.getFileHashName();

            } else if(type.endsWith(IMPORT_BANK)) {
                BankData bank = BankData.createWithTransfer(type, AccConstant.TRX_EXCEL, accountBankMapper, null);
                bank.excelParse(file);
                bank.transferData();
                fileHash = bank.getFileHashName();
            }

            // 결과 반환
            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_SUCCESS,
                    "message", AccConstant.CM_SUCCESS,
                    "fileId", fileHash
            );
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_FAILED,
                    "message", ioe.getMessage()
            );
        } catch(Exception e) {
            e.printStackTrace();
            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_FAILED,
                    "message", e.getMessage()
            );
        }
    }

    /**
     * 본 테이블로 이관
     * @param data
     * @return
     */
    public Map<String, Object> transferData(Map<String, Object> data) {
        List<Map<String, Object>> statementMap = (List<Map<String, Object>>)data.get("data");
        String type = MapUtils.getString(data, "type");
        String fileHash = MapUtils.getString(data, "fileHash");
        boolean resFlag = true;

        String result = AccConstant.CM_SUCCESS;
        String message = AccConstant.CM_SUCCESS;

        try {
            if(type.endsWith(IMPORT_CARD)) {
                CardData card = CardData.createWithTransfer(type, AccConstant.TRX_MAIN, accountCardMapper, null);
                card.setFileHashName(fileHash);
                card.setCardStatementsWithMap(statementMap);
                resFlag = card.transferData();

            } else if(type.endsWith(IMPORT_BANK)) {
                BankData bank = BankData.createWithTransfer(type, AccConstant.TRX_MAIN, accountBankMapper, null);
                bank.setFileHashName(fileHash);
                bank.setBankStatementsWithMap(statementMap);
                resFlag = bank.transferData();
            } else {
                throw new Exception("Invalid Type Error....");
            }

            if (!resFlag) {
                throw new Exception("COMPLETED SIZE IS DIFFERENT");
            }

        } catch(Exception e) {
            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_FAILED,
                    "message", e.getMessage()
            );
        }

        return ImmutableMap.of(
                AccConstant.CM_RESULT, result,
                "message", message
        );
    }

    public Map<String, Object> transferInfoData(Map<String, Object> data) {
        List<Map<String, Object>> statementMap = (List<Map<String, Object>>)data.get("data");
        String type = MapUtils.getString(data, "type");
        boolean resFlag = true;

        try {
            if(type.endsWith(IMPORT_CARD)) {
                CardData card = CardData.createWithInfo(cardInfoMapper);
                card.setCardInfoStatementsWithMap(statementMap);
                resFlag = card.transferData();

            } else if(type.endsWith(IMPORT_BANK)) {
                BankData bank = BankData.createWithInfo(bankInfoMapper);
                bank.setBankInfoStatementsWithMap(statementMap);
                resFlag = bank.transferData();
            } else {
                throw new Exception("Invalid Type Error...");
            }

            if (!resFlag) {
                throw new Exception("COMPLETED SIZE IS DIFFERENT");
            }

            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_SUCCESS
            );
        } catch(Exception e) {
            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_FAILED,
                    "message", Optional.ofNullable(e.getMessage()).orElse("error of error")
            );
        }
    }

    /**
     * 은행 및 카드 관리정보 조회
     * @param param
     * @return
     */
    public Map<String, Object> getInfoDataList(Map<String, Object> param) {
        String type = MapUtils.getString(param, "type");

        try {
            if (type.equals(IMPORT_CARD)) {
                CardData card = CardData.createWithInfo(cardInfoMapper);
                card.prepareInfoStatementByCondition(param);

                return ImmutableMap.of(
                        AccConstant.CM_RESULT, AccConstant.CM_SUCCESS,
                        AccConstant.CM_DATA, card.getCardInfoStatements()
                        );
            } else {
                throw new Exception("Invalid Type Error...");
            }
        } catch(Exception e) {
            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_FAILED,
                    "message", e.getMessage()
            );
        }
    }

    /**
     * 임시저장된 파일리스트 반환
     * @param fileType
     * @return
     */
    public List<Map<String, Object>> getFileHashList(String fileType) {
        return accountCardMapper.selectTmpFileHashName(fileType);
    }

    /**
     * 최근 신용카드 사용내역 (recently 5)
     */
    public List<Map<String, Object>> getRecentUseCardStatementList() {
        return accountCardMapper.selectRecentCardStatement();
    }

    /**
     * 카드 별 이번 달 잔액
     * @return List
     */
    public List<Map<String, Object>> getRemainAmountByCard() {
        List<Map<String, Object>> cardRemainDataList = new ArrayList<>();
        Map<String, Object> schParam = new HashMap<>();

        try {
            CardData cardData = CardData.createWithInfo(cardInfoMapper);
            cardData.prepareInfoStatementByCondition(schParam);

            cardData.getCardInfoStatements()
                    .stream()
                    .filter(card -> StringUtils.isEquals(AccConstant.YES, card.getSelectedMain()))
                    .forEach(card -> {
                        Map<String, Object> amtParam = new HashMap<>();
                        Map<String, Object> remain = new HashMap<>();

                        amtParam.put("ym", DateUtils.getTodayStr("yyyyMM"));
                        amtParam.put("cardNo", card.getCardNo());
                        Long usedAmount = accountCardMapper.selectUsedAmountByYm(amtParam);
                        if (usedAmount == null) {
                            usedAmount = 0L;
                        }

                        double percentage = ((double) usedAmount / card.getLimitAmt()) * 100;

                        remain.put("cardNm", card.getCardName());
                        remain.put("limitedAmt", card.getLimitAmt());
                        remain.put("usedAmt", usedAmount);
                        remain.put("usedPercentage", String.format("%.2f", percentage));
                        remain.put("remainAmt", card.getLimitAmt() - usedAmount);

                        cardRemainDataList.add(remain);
                    });

            return cardRemainDataList;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 카드 건 별 결제내역 제출 -> card_statement_sms
     * @param param
     * @return
     */
    public Map<String, Object> registCardPaymentText(Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>();
        boolean flag = true;

        // 결제내역 텍스트 추출
        String smsText = MapUtils.getString(param, "text");
        String type = MapUtils.getString(param, "type");

        try {
            if(type.equals(IMPORT_CARD)) {
                CardData card = CardData.createWithTransfer(type, AccConstant.TRX_SMS, accountCardMapper, cardInfoMapper);
                card.textParse(type, smsText);
                flag = card.transferData();

            } else if(type.equals(IMPORT_BANK)) {
                BankData bank = BankData.createWithTransfer(type, AccConstant.TRX_SMS, accountBankMapper, bankInfoMapper);
                bank.textParse(type, smsText);
                flag = bank.transferData();

            }

            if(!flag) {
                throw new Exception("SMS Text Registration Failed");
            }
        } catch (Exception e) {
            result.put(AccConstant.CM_RESULT, AccConstant.CM_FAILED);
            result.put("message", e.getMessage());
        }
        return ImmutableMap.of(AccConstant.CM_RESULT, AccConstant.CM_SUCCESS, AccConstant.CM_DATA, result);
    }

    /**
     * tmp 테이블에 저장한 후 fileHash 값으로 임시저장된 리스트 조회
     * @param fileId
     * @return
     */
    public Map<String, Object> getImportExcelDataList(String type, String fileId) {
        try {
            // CARD
            if(type.equals(IMPORT_CARD)) {
                CardData card = CardData.createWithDefault(type, accountCardMapper);
                card.prepareStatementTmpByFileId(fileId);

                return ImmutableMap.of(
                        AccConstant.CM_RESULT, AccConstant.CM_SUCCESS,
                        "data", card.getCardStatements());

            // BANK
            } else if(type.equals(IMPORT_BANK)) {
                BankData bank = BankData.createWithDefault(type, accountBankMapper);
                bank.prepareStatementTmpByFileId(fileId);

                return ImmutableMap.of(
                        AccConstant.CM_RESULT, AccConstant.CM_SUCCESS,
                        "data", bank.getBankStatements());
            } else {
                throw new Exception("Invalid Parameter Error");
            }
        } catch(Exception e) {
            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_FAILED,
                    "message", e.getMessage()
            );
        }
    }
}

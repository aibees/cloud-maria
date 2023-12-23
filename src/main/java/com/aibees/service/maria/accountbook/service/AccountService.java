package com.aibees.service.maria.accountbook.service;

import com.aibees.service.maria.accountbook.entity.dto.CardDto;
import com.aibees.service.maria.accountbook.entity.mapper.AccountBankMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountCardInfoMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountCardMapper;
import com.aibees.service.maria.accountbook.entity.vo.BankStatement;
import com.aibees.service.maria.accountbook.entity.vo.CardInfoStatement;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.accountbook.util.handler.ExcelParseHandler;
import com.aibees.service.maria.common.DateUtils;
import com.aibees.service.maria.common.MapUtils;
import com.aibees.service.maria.common.StringUtils;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.aibees.service.maria.accountbook.util.AccConstant.IMPORT_BANK;
import static com.aibees.service.maria.accountbook.util.AccConstant.IMPORT_CARD;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountBankMapper accountBankMapper;
    private final AccountCardMapper accountCardMapper;
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
    public List<CardStatement> getCardStatementList(CardDto cardParam) {

        // handling parameter
        // 1. usage Type
        if(StringUtils.isEquals(cardParam.getUsage(), "-1")) {
            cardParam.setUsage(AccConstant.EMPTY_STR); // 전체 Usage 값 들어오면 empty로 변경
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
            List<CardStatement> cardStatementList = null;
            List<BankStatement> bankStatementList = null;

            Workbook workbook = null;
            String[] fileName = file.getOriginalFilename().split("\\.");
            String fileHashName = this.createFileNameHash();

            if (fileName[fileName.length-1].equals("xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (fileName[fileName.length-1].equals("xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }

            if(workbook == null) {
                return ImmutableMap.of(
                        AccConstant.CM_RESULT, AccConstant.CM_FAILED,
                        "message", "WORKBOOK is null",
                        "data", null
                );
            }

            if(type.endsWith(IMPORT_CARD)) {
                cardStatementList = (List<CardStatement>)ExcelParseHandler.excelParser(workbook, fileHashName, type).get(AccConstant.CM_RESULT);

                if(cardStatementList != null) {
                    if(!insertToCardExcelTmp(cardStatementList)) {
                        throw new Exception("Error Occured in Excel Tmp Insert");
                    }
                } else { throw new Exception("CardStatement is Null"); }

            } else if(type.endsWith(IMPORT_BANK)) {
                bankStatementList = (List<BankStatement>)ExcelParseHandler.excelParser(workbook, fileHashName, type).get(AccConstant.CM_RESULT);

                if(bankStatementList != null) {
                    if(!insertToBankExcelTmp(bankStatementList)) {
                        throw new Exception("Error Occured in Excel Tmp Insert");
                    }
                } else { throw new Exception("CardStatement is Null"); }
            }

            // 파일명 저장
            System.out.println("===== fileName : " + file.getOriginalFilename() + " =====");
            accountCardMapper.insertTmpFileHashName(ImmutableMap.of(
                    "fileId", fileHashName,
                    "fileType", IMPORT_CARD,
                    "fileName", file.getOriginalFilename()
            ));

            // 결과 반환
            return ImmutableMap.of(
                    AccConstant.CM_RESULT, AccConstant.CM_SUCCESS,
                    "message", AccConstant.CM_SUCCESS,
                    "fileId", fileHashName
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
        List<Map<String, Object>> statmentList = (List<Map<String, Object>>)data.get("data");
        String fileHash = data.get("fileHash").toString();
        String result = AccConstant.CM_SUCCESS;
        String message = AccConstant.CM_SUCCESS;

        try {
            int dataSize = data.size();
            boolean resFlag = true;
            if (dataSize > 0) {
                resFlag = insertToMain(statmentList);
                if(resFlag)
                    accountCardMapper.deleteCardStatementTmp(fileHash);
            }

            if (!resFlag) {
                result = AccConstant.CM_FAILED;
                message = "COMPLETED SIZE IS DIFFERENT";
            }

            accountCardMapper.deleteTmpFileHashName(ImmutableMap.of(
                "fileId", fileHash,
                "fileType", IMPORT_CARD
            ));

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

        cardInfoMapper.selectCardInfoListByCondition()
                .stream()
                .filter(card -> StringUtils.isEquals(AccConstant.YES, card.getSelectedMain()))
                .forEach(card -> {
                    Map<String, Object> amtParam = new HashMap<>();
                    Map<String, Object> remain = new HashMap<>();

                    amtParam.put("ym", DateUtils.getTodayStr("yyyyMM"));
                    amtParam.put("cardNo", card.getCardNo());
                    Long usedAmount = accountCardMapper.selectUsedAmountByYm(amtParam);
                    if(usedAmount == null) {
                        usedAmount = 0L;
                    }

                    double percentage = ((double)usedAmount / card.getLimitAmt())*100;

                    remain.put("cardNm", card.getCardName());
                    remain.put("limitedAmt", card.getLimitAmt());
                    remain.put("usedAmt", usedAmount);
                    remain.put("usedPercentage", String.format("%.2f", percentage));
                    remain.put("remainAmt", card.getLimitAmt() - usedAmount);

                    cardRemainDataList.add(remain);
                });

        return cardRemainDataList;
    }

    /**
     * 카드 건 별 결제내역 제출 -> card_statement_sms
     * @param param
     * @return
     */
    public Map<String, Object> registCardPaymentText(Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>();

        // 결제내역 텍스트 추출
        String cardText = MapUtils.getString(param, "text");

        // text split (newLine 먼저, newLine 없는 한줄형태면 space로 parse
        String[] paramText;
        if(cardText.contains(AccConstant.NEWLINE_STR)) {
            paramText = cardText.split(AccConstant.NEWLINE_STR);
        } else {
            paramText = cardText.split(AccConstant.SPACE_STR);
        }

        CardStatement paramStatement = getCardStatementByDailyPaymentHandler(paramText);

        if(Objects.isNull(paramStatement)) {
            result.put(AccConstant.CM_RESULT, "FAIL");
            result.put("message", "정의되지 않은 형식이거나 없는 카드정보입니다.");
        }

        // insert
        int insertRet;
        try {
            insertRet = accountCardMapper.insertCardStatementSms(paramStatement);
            if(insertRet == 1) {
                result.put(AccConstant.CM_RESULT, "SUCCESS");
                result.put("message", "SUCCESS");
            }
        } catch (DataIntegrityViolationException | SQLIntegrityConstraintViolationException sqle) {
            result.put(AccConstant.CM_RESULT, "FAIL");
            result.put("message", sqle.getMessage());
        }
        return result;
    }

    /******************************
     ****** Private Function ******
     ******************************/

    /**
     * Exceltmp 테이블로 전송 (건 별로 insert)
     * @param datalist
     * @return boolean
     */
    private boolean insertToCardExcelTmp(List<CardStatement> datalist) {
        AtomicInteger resultCnt = new AtomicInteger(0);
        int listCnt = datalist.size();
        boolean result = true;

        datalist.forEach(state -> {
            int insertResult = accountCardMapper.insertCardStatementTmp(state);
            resultCnt.addAndGet(insertResult);
        });

        if(listCnt != resultCnt.get()) {
            result = false;
        }
        return result;
    }

    /**
     * Exceltmp 테이블로 전송 (건 별로 insert) - bank
     * @param datalist
     * @return boolean
     */
    private boolean insertToBankExcelTmp(List<BankStatement> datalist) {
        AtomicInteger resultCnt = new AtomicInteger(0);
        int listCnt = datalist.size();
        boolean result = true;

        datalist.forEach(state -> {
            int insertResult = accountBankMapper.insertBankStatementTmp(state);
            resultCnt.addAndGet(insertResult);
        });

        if(listCnt != resultCnt.get()) {
            result = false;
        }
        return result;
    }

    /**
     * 가공 완료된 데이터를 본 테이블로 옮기는 method
     * @param datalist
     * @return
     */
    private boolean insertToMain(List<Map<String, Object>> datalist) {
        AtomicInteger resultCnt = new AtomicInteger(0);
        int listCnt = datalist.size();
        boolean result = true;

        datalist.forEach(state -> {
            int insertResult = accountCardMapper.insertCardStatement(state);
            resultCnt.addAndGet(insertResult);
        });

        if(listCnt > resultCnt.get()) { // duplicate key 경우 2를 반환하기에 해당 경우의 수도 염두두
           result = false;
        }
        return result;
    }

    /**
     * tmp 테이블에 저장한 후 fileHash 값으로 임시저장된 리스트 조회
     * @param fileId
     * @return
     */
    public List<CardStatement> getImportExcelDataList(String fileId) {
        return accountCardMapper.getImportedCardStatementTmp(fileId);
    }

    /**
     * tmp 테이블에서 파일 단위로 조회하기 위한 임시 Hash Value
     * @return String
     */
    private String createFileNameHash() {
        return LocalDateTime
                .now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                .concat(StringUtils.getRandomStr(4));
    }

    private CardStatement getCardStatementByDailyPaymentHandler(String[] param) {
        CardStatement statementParam = null;
        if(param[0].contains("하나")) {
            // 하나카드 기준으로 parsing
            // 1. card info
            Map<String, Object> cardInfoParam = new HashMap<>();
            cardInfoParam.put("company", param[0].substring(0, 2));
            cardInfoParam.put("cardNo", param[0].substring(2, 6).replace("*", "%"));
            CardInfoStatement cardInfo = cardInfoMapper.selectCardInfoByCondition(cardInfoParam);

            // 2. amount
            String amount = param[2].replace(",", "").replace("원", "");

            // 3. ymd & times
            String ymd = DateUtils.getTodayStr("yyyy") + param[4].replace("/", "");
            String times = param[5].replace(":", "").concat("00");

            // 4. remark
            String remark = param[6];

            statementParam = CardStatement.builder()
                    .ymd(ymd)
                    .times(times)
                    .amount(Long.parseLong(amount))
                    .remark(remark)
                    .cardNo(cardInfo.getCardNo())
                    .cardNm(cardInfo.getCardName())
                    .build();

        } else if(param[0].contains("현대")) {
            // 현대카드 parsing
            // 1. cardInfo
            String[] splitOfCard = param[0].split(AccConstant.SPACE_STR);

            Map<String, Object> cardInfoParam = new HashMap<>();
            cardInfoParam.put("cardName", splitOfCard[1]);
            cardInfoParam.put("company", splitOfCard[0].substring(0, 2));
            CardInfoStatement cardInfo = cardInfoMapper.selectCardInfoByCondition(cardInfoParam);

            if(Objects.isNull(cardInfo)) {
                return null;
            }

            // 2. amount
            String amount = param[2].split(AccConstant.SPACE_STR)[0].replace(",", "").replace("원", "");

            // 3. ymd & times
            String[] ymdTimes = param[3].split(AccConstant.SPACE_STR);
            String ymd = DateUtils.getTodayStr("yyyy") + ymdTimes[0].replace("/", "");
            String times = ymdTimes[1].replace(":", "").concat("00");

            // 4. remark
            String remark = param[4];

            statementParam = CardStatement.builder()
                    .ymd(ymd)
                    .times(times)
                    .amount(Long.parseLong(amount))
                    .remark(remark)
                    .cardNo(cardInfo.getCardNo())
                    .cardNm(cardInfo.getCardName())
                    .build();
        }

        return statementParam;
    }
}

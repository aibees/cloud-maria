package com.aibees.service.maria.accountbook.service;

import com.aibees.service.maria.accountbook.entity.dto.CardDto;
import com.aibees.service.maria.accountbook.entity.mapper.AccountMapper;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.accountbook.util.handler.ExcelParseHandler;
import com.aibees.service.maria.common.StringUtils;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;

    public List<CardStatement> getCardStatementList(CardDto cardParam) {
        return accountMapper.selectCardStatementList(cardParam);
    }

    /**
     * 엑셀 업로드 파일 파싱 후 Insert, 재조회
     * @param String, MultipartFile
     * @param Map
     */
    public Map<String, Object> excelParse(String type, MultipartFile file) {
        System.out.println("type : " + type);
        try {
            List<CardStatement> cardStatementList = null;

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
                        "result", "FAILED",
                        "message", "WORKBOOK is null",
                        "data", null
                );
            }

            cardStatementList = ExcelParseHandler.excelParseCard(workbook, fileHashName, type);

            if(cardStatementList != null) {
                if(!insertToExcelTmp(cardStatementList)) {
                    throw new Exception("Error Occured in Excel Tmp Insert");
                }
            } else {
                throw new Exception("CardStatement is Null");
            }

            return ImmutableMap.of(
                    "result", "SUCCESS",
                    "message", "SUCCESS",
                    "fileId", fileHashName
            );
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return ImmutableMap.of(
                    "result", "FAILED",
                    "message", ioe.getMessage()
            );
        } catch(Exception e) {
            e.printStackTrace();
            return ImmutableMap.of(
                    "result", "FAILED",
                    "message", e.getMessage()
            );
        }
    }

    public Map<String, Object> transferData(Map<String, Object> data) {
        List<Map<String, Object>> statmentList = (List<Map<String, Object>>)data.get("data");
        String fileHash = data.get("fileHash").toString();
        String result = "SUCCESS";
        String message = "SUCCESS";

        try {
            int dataSize = data.size();
            AtomicInteger resultCnt = new AtomicInteger(0);
            boolean resFlag = true;
            if (dataSize > 0) {
                resFlag = insertToMain(statmentList);
                if(resFlag)
                    accountMapper.deleteCardStatementTmp(fileHash);
            }

            if (!resFlag) {
                result = "FAILED";
                message = "COMPLETED SIZE IS DIFFERENT";
            }
        } catch(Exception e) {
            return ImmutableMap.of(
                    "result", "FAILED",
                    "message", e.getMessage()
            );
        }

        return ImmutableMap.of(
                "result", result,
                "message", message
        );
    }

    /******************************
     ****** Private Function ******
     ******************************/

    /**
     * Exceltmp 테이블로 전송 (건 별로 insert)
     * @param datalist
     * @return boolean
     */
    private boolean insertToExcelTmp(List<CardStatement> datalist) {
        AtomicInteger resultCnt = new AtomicInteger(0);
        int listCnt = datalist.size();
        boolean result = true;

        datalist.forEach(state -> {
            int insertResult = accountMapper.insertCardStatementTmp(state);
            resultCnt.addAndGet(insertResult);
        });

        if(listCnt != resultCnt.get()) {
            result = false;
        }
        return result;
    }

    private boolean insertToMain(List<Map<String, Object>> datalist) {
        AtomicInteger resultCnt = new AtomicInteger(0);
        int listCnt = datalist.size();
        boolean result = true;

        datalist.forEach(state -> {
            int insertResult = accountMapper.insertCardStatement(state);
            resultCnt.addAndGet(insertResult);
        });

        if(listCnt > resultCnt.get()) { // duplicate key 경우 2를 반환하기에 해당 경우의 수도 염두두
           result = false;
        }
        return result;
    }

    public List<CardStatement> getImportExcelDataList(String fileId) {
        return accountMapper.getImportedCardStatementTmp(fileId);
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
}

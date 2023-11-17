package com.aibees.service.maria.accountbook.service;

import com.aibees.service.maria.accountbook.entity.dto.CardDto;
import com.aibees.service.maria.accountbook.entity.mapper.AccountMapper;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
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
     * @param type
     * @param file
     */
    public Map<String, Object> excelParse(String type, MultipartFile file) {
        System.out.println("type : " + type);
        Map<String, Object> result = new HashMap<>();
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

            if ("HANA".equals(type)) {
                cardStatementList = Objects.requireNonNull(excelParseForHana(workbook, fileHashName))
                        .stream()
                        .filter(state -> state.getApYn().equals("매입") || state.getStatus().equals("정상"))
                        .collect(Collectors.toList());
            } else if("SAMSUNG".equals(type)) {
                cardStatementList = Objects.requireNonNull(excelParseForSamsung(workbook, fileHashName));
            } else if("HYUNDAE".equals(type)) {
                cardStatementList = Objects.requireNonNull(excelParseForHyundae(workbook, fileHashName));
            }

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
            return ImmutableMap.of(
                    "result", "FAILED",
                    "message", ioe.getCause()
            );
        } catch(Exception e) {
            return ImmutableMap.of(
                    "result", "FAILED",
                    "message", e.getMessage()
            );
        }
    }

    private List<CardStatement> excelParseForHyundae(Workbook workbook, String fileHash) throws IOException {
        return null;
    }

    private List<CardStatement> excelParseForSamsung(Workbook workbook, String fileHash) throws IOException {
        return null;
    }

    private List<CardStatement> excelParseForHana(Workbook workbook, String fileHash) throws IOException {
        int HEADER_ROW = 3;
        AtomicInteger DATA_ROW = new AtomicInteger(4);

        Sheet dataSheet = workbook.getSheetAt(0);
        List<String> colList = new ArrayList<>();
        Map<Integer, List<String>> dataListByRow = new HashMap<>();

        int colCnt = 0;
        Row headerRow = dataSheet.getRow(HEADER_ROW);

        while(true) {
            String c;
            try {
                c = headerRow.getCell(colCnt).getStringCellValue().replace("\n", " ");
            } catch (NullPointerException npe) {
                break;
            }
            if(StringUtils.isNull(c)) {
                break;
            }
            colList.add(c);
            colCnt++;
        }

        while(true) {
            int whileExit = 0;
            Row currentRow = dataSheet.getRow(DATA_ROW.get());
            List<String> rowData = new ArrayList<>();

            int idx = 0;
            String d;
            for(; idx < colList.size(); idx++) {

                try {
                    d = getCellDataToStr(currentRow.getCell(idx));
                } catch (NullPointerException npe) {
                    whileExit = -1;
                    break;
                }
                if(idx == 0 && StringUtils.isNull(d)) {
                    whileExit = -1;
                    break;
                }

                rowData.add(d);
            }

            if(whileExit < 0)
                break;

            dataListByRow.put(DATA_ROW.get()-4, rowData);
            DATA_ROW.set(DATA_ROW.get()+1);
        }


        return dataListByRow.keySet().stream().map(k -> {
            List<String> data = dataListByRow.get(k);

            return CardStatement.builder()
                    .fileHash(fileHash)
                    .ymd(data.get(0).replace(".", ""))
                    .times(data.get(1).replace(":", ""))
                    .cardNo(data.get(2).split(" ")[1])
                    .approvNum(data.get(3))
                    .remark(data.get(4))
                    .amount(Integer.parseInt(data.get(5).split("\\.")[0]))
                    .apYn(data.get(9))
                    .status(data.get(13))
                    .usageCd("FF")
                    .build();
        }).collect(Collectors.toList());
    }

    public boolean insertToExcelTmp(List<CardStatement> datalist) {
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

    public String createFileNameHash() {
        return LocalDateTime
                .now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                .concat(StringUtils.getRandomStr(4));
    }

    public List<CardStatement> getImportExcelDataList(String fileId) {
        return accountMapper.getImportedCardStatementTmp(fileId);
    }

    private String getCellDataToStr(Cell cell) {
        if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue().replace("\n", " ");
        } else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return Double.toString(cell.getNumericCellValue());
        } else {
            return cell.getDateCellValue().toString();
        }
    }
}

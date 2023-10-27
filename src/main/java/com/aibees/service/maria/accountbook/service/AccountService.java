package com.aibees.service.maria.accountbook.service;

import com.aibees.service.maria.accountbook.entity.dto.CardDto;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.common.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AccountService {

    public List<CardStatement> getCardStatementList(CardDto param) {
        return null;
    }

    /**
     * 엑셀 업로드 파일 파싱 후 Insert, 재조회
     * @param type
     * @param file
     */
    public void excelParse(String type, MultipartFile file) {
        System.out.println("type : " + type);
        try {
            List<CardStatement> cardStatementList = null;

            Workbook workbook = null;
            String[] fileName = file.getOriginalFilename().split("\\.");

            if (fileName[fileName.length-1].equals("xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (fileName[fileName.length-1].equals("xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }

            if(workbook == null) {
                return;
            }

            if ("HANACARD".equals(type)) {
                cardStatementList = Objects.requireNonNull(excelParseForHana(workbook))
                        .stream()
                        .filter(state -> state.getApYn().equals("매입") || state.getStatus().equals("정상"))
                        .collect(Collectors.toList());
            } else if("SAMSUNG".equals(type)) {
                cardStatementList = Objects.requireNonNull(excelParseForSamsung(workbook));
            } else if("HYUNDAE".equals(type)) {
                cardStatementList = Objects.requireNonNull(excelParseForHyundae(workbook));
            }

            if(cardStatementList != null) {

            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private List<CardStatement> excelParseForHyundae(Workbook workbook) throws IOException {
        return null;
    }

    private List<CardStatement> excelParseForSamsung(Workbook workbook) throws IOException {
        return null;
    }

    private List<CardStatement> excelParseForHana(Workbook workbook) throws IOException {
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
                    .ymd(data.get(0).replace(".", ""))
                    .times(data.get(1).replace(":", ""))
                    .cardNo(data.get(2).split(" ")[1])
                    .approvNum(data.get(3))
                    .remark(data.get(4))
                    .amount(Integer.parseInt(data.get(5).split("\\.")[0]))
                    .apYn(data.get(9))
                    .status(data.get(13))
                    .usageNm("미분류")
                    .usageCd("FF")
                    .build();
        }).collect(Collectors.toList());
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

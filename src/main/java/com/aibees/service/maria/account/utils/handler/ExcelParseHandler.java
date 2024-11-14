package com.aibees.service.maria.account.utils.handler;

import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.common.utils.StringUtils;
import com.google.common.math.DoubleMath;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelParseHandler {
    private ExcelParseHandler() {}

    private static ExcelParser parser;

    public static Map<String, Object> excelParser(Workbook workbook, String fileHash, String type) {
        try {
            getExcelParser(type);
            return parser.parse(workbook, fileHash);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /******************************
     ** Common Method  For Parse **
     ******************************/
    // 컬럼 헤더 조회
    public static List<String> getHeaderList(Row headerRow) {
        log.info("======== get Header List =======");
        List<String> colList = new ArrayList<>();
        int colCnt = 0;
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

        return colList;
    }

    // 데이터 Row 별 데이터 추출
    public static Map<Integer, List<String>> getDataList(Sheet dataSheet, List<String> colList, int dataRow) {

        log.info("======== get Data List =======");
        Map<Integer, List<String>> dataListByRow = new HashMap<>();
        int curRow = dataRow;
        while(true) {
            int whileExit = 0;
            Row currentRow = dataSheet.getRow(curRow);
            List<String> rowData = new ArrayList<>();

            int idx = 0;
            String d;
            // 컬럼 리스트 순으로 cell data 추출
            for(; idx < colList.size(); idx++) {

                try {
                    d = getCellDataToStr(currentRow.getCell(idx));
                } catch (NullPointerException npe) {
                    whileExit = -1;
                    break;
                }
                if(idx == 0 && ( StringUtils.isNull(d.trim()) || StringUtils.isEquals("-", d))) {
                    whileExit = -1;
                    break;
                }

                rowData.add(d);
            }

            if(whileExit < 0) {
                break;
            }

            dataListByRow.put(curRow - dataRow, rowData);

            if(whileExit < 0)
                break;
            curRow += 1;
        }

        return dataListByRow;
    }

    private static void getExcelParser(String type) throws Exception {
        String parserPrefix = "com.aibees.service.maria.account.utils.handler.ExcelParserFor";
        Class<ExcelParser> parserClass = (Class<ExcelParser>) Class.forName(String.join(".", parserPrefix, type));

        parser = parserClass.getDeclaredConstructor().newInstance();
    }

    private static String getCellDataToStr(Cell cell) {
        if(cell == null) {
            return AccConstant.EMPTY_STR;
        }

        if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue().replace("\n", " ");
        } else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            double numericData = cell.getNumericCellValue();

            // Integer 라면
            if(DoubleMath.isMathematicalInteger(numericData)) {
                return String.valueOf((int)numericData);
            } else {
                // 그대로 소숫점이 있다면면
               return Double.toString(cell.getNumericCellValue());
            }
        } else {
            return cell.getDateCellValue().toString();
        }
    }
}

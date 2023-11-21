package com.aibees.service.maria.accountbook.util.handler;

import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.common.StringUtils;
import com.google.common.base.Strings;
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
import java.util.stream.Collectors;

@Slf4j
public class ExcelParseHandler {
    private ExcelParseHandler() {}

    public static List<CardStatement> excelParseCard(Workbook workbook, String fileHash, String type) {
        if(AccConstant.CARD_HANA.equals(type)) {
            return parseForHanaCard(workbook, fileHash);
        }

        if(AccConstant.CARD_HYUNDAI.equals(type)) {
            return parseForHyundaiCard(workbook, fileHash);
        }

        return null;
    }

    /**
     * 하나카드 엑셀 parsing
     * @param workbook
     * @param fileHash
     * @return
     */
    private static List<CardStatement> parseForHanaCard(Workbook workbook, String fileHash) {
        int HEADER_ROW = 3;
        int DATA_ROW = 4;

        Sheet dataSheet = workbook.getSheetAt(0);
        List<String> colList = getHeaderList(dataSheet.getRow(HEADER_ROW));
        Map<Integer, List<String>> dataListByRow = getDataList(dataSheet, colList, DATA_ROW);

        return dataListByRow.keySet().stream()
                .map(k -> {
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
                })
                .filter(state -> state.getApYn().equals("매입") || state.getStatus().equals("정상"))
                .collect(Collectors.toList());
    }


    private static List<CardStatement> parseForHyundaiCard(Workbook workbook, String fileHash) {
        int HEADER_ROW = 2;
        int DATA_ROW = 3;

        Sheet dataSheet = workbook.getSheetAt(0);
        List<String> colList = getHeaderList(dataSheet.getRow(HEADER_ROW));
        Map<Integer, List<String>> dataListByRow = getDataList(dataSheet, colList, DATA_ROW);
        System.out.println("HYUNDAI Parsing is Completed,,, size : " + dataListByRow.size());
        return dataListByRow.keySet().stream()
                .map(k -> {
                    List<String> data = dataListByRow.get(k);
                    String cardNoStr = data.get(2).substring(data.get(2).length()-5, data.get(2).length()-2);

                    return CardStatement.builder()
                            .fileHash(fileHash)
                            .ymd(StringUtils.dateStrParseToYMD(data.get(0), "-")) // 날짜
                            .times(AccConstant.DEFAULT_TIMES.replace(":", "")) // 시간(없음)
                            .cardNo(cardNoStr)
                            .approvNum(Strings.padStart(data.get(9), 8, '0')) // 승인번호
                            .remark(data.get(3)) // 가맹점명
                            .amount(Integer.parseInt(data.get(6).split("\\.")[0]))
                            .apYn("매입")
                            .status("정상")
                            .usageCd("FF")
                            .build();
                })
                .filter(state -> state.getApYn().equals("매입") || state.getStatus().equals("정상"))
                .collect(Collectors.toList());
    }

    /******************************
     ** Common Method  For Parse **
     ******************************/
    // 컬럼 헤더 조회
    private static List<String> getHeaderList(Row headerRow) {
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
    private static Map<Integer, List<String>> getDataList(Sheet dataSheet, List<String> colList, int dataRow) {

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
                if(idx == 0 && (StringUtils.isNull(d) || StringUtils.isEquals("-", d))) {
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

package com.aibees.service.maria.accountbook.util.handler;

import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.common.StringUtils;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelParserForHYUNDAICARD implements ExcelParser {
    @Override
    public Map<String, Object> parse(Workbook workbook, String fileHash) {
        int HEADER_ROW = 2;
        int DATA_ROW = 3;

        Sheet dataSheet = workbook.getSheetAt(0);
        List<String> colList = ExcelParseHandler.getHeaderList(dataSheet.getRow(HEADER_ROW));
        Map<Integer, List<String>> dataListByRow = ExcelParseHandler.getDataList(dataSheet, colList, DATA_ROW);

        System.out.println("HYUNDAI Parsing is Completed,,, size : " + dataListByRow.size());

        List<CardStatement> statementList = dataListByRow.keySet().stream()
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
                            .amount(Long.parseLong(data.get(6).split("\\.")[0]))
                            .apYn("매입")
                            .status("정상")
                            .usageCd("FF")
                            .build();
                })
                .filter(state -> state.getApYn().equals("매입") || state.getStatus().equals("정상"))
                .collect(Collectors.toList());

        return ImmutableMap.of(AccConstant.CM_RESULT, statementList);
    }
}

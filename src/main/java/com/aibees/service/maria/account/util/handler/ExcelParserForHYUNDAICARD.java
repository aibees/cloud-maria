package com.aibees.service.maria.account.util.handler;

import com.aibees.service.maria.account.domain.entity.card.CardStatementTmp;
import com.aibees.service.maria.account.utils.constant.AccConstant;
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

        List<CardStatementTmp> statementList = dataListByRow.keySet().stream()
                .map(k -> {
                    List<String> data = dataListByRow.get(k);
                    String ymd = data.get(0)
                            .replace(AccConstant.SPACE_STR, AccConstant.EMPTY_STR)
                            .replace("년", "")
                            .replace("월", "")
                            .replace("일", "");

                    String cardNoStr = data.get(3).substring(data.get(3).length()-4).replace("*", "%");

                    System.out.println("ymd : " + ymd + ", cardNo : " + cardNoStr);
                    return CardStatementTmp.builder()
                            .fileHash(fileHash)
                            .ymd(ymd) // 날짜
                            .times(trimTimes(data.get(1)))
                            .cardNo(cardNoStr)
                            .approvNum(Strings.padStart(data.get(8), 8, '0')) // 승인번호
                            .remark(data.get(4)) // 가맹점명
                            .amount(Long.parseLong(data.get(5).replace(",", "")))
                            .apYn("매입")
                            .status("정상")
                            .usageCd(getUsageWithType(data.get(4)))
                            .build();
                })
                .filter(state -> state.getApYn().equals("매입") || state.getStatus().equals("정상"))
                .collect(Collectors.toList());

        return ImmutableMap.of(AccConstant.CM_RESULT, statementList);
    }

    private String trimTimes(String src) {
        System.out.println("src : " + src);
        String[] strSplit = src.split(":");
        String hh = (strSplit[0].length() == 1 ? "0" : "") + strSplit[0];
        System.out.println("strSplit : " + strSplit[0] + " / " + strSplit[1]);
        return hh + strSplit[1] + "00";
    }

    private String getUsageWithType(String remark) {
        String usage = "FF";

        if(remark.contains("주유")) {
            return "0E";
        }

        if(remark.contains("도로공사") || remark.contains("고속도로")) {
            return "0D";
        }
        return usage;
    }
}
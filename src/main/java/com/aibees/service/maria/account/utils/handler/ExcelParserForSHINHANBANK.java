package com.aibees.service.maria.account.utils.handler;

import com.aibees.service.maria.account.domain.entity.bank.BankStatementTmp;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.common.StringUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelParserForSHINHANBANK implements ExcelParser {
    @Override
    public Map<String, Object> parse(Workbook workbook, String fileHash) {
        int HEADER_ROW = 6;
        int DATA_ROW = 7;

        Sheet dataSheet = workbook.getSheetAt(0);
        List<String> colList = ExcelParseHandler.getHeaderList(dataSheet.getRow(HEADER_ROW));
        Map<Integer, List<String>> dataListByRow = ExcelParseHandler.getDataList(dataSheet, colList, DATA_ROW);

        System.out.println("SHINHANBANK Parsing is Completed,,, size : " + dataListByRow.size());
        String bankAcct = dataSheet.getRow(2).getCell(1).toString().replace("-", "");

        List<BankStatementTmp> statementList = dataListByRow.keySet().stream()
                .map(k -> {
                    List<String> data = dataListByRow.get(k);

                    // 수입 : 0 / 지출 : 1
                    String entryCd = (data.get(3).equals("0") || StringUtils.isNull(data.get(3)))? "0" : "1"; // 출금 쪽이 0이면 수입(0), 아니면 지출(1)

                    return BankStatementTmp.builder()
                            .fileHash(fileHash)
                            .ymd(data.get(0).replace("-", ""))
                            .times(data.get(1).replace(":", ""))
                            .entryCd(entryCd)
                            .usageCd(getUsageWithType(data.get(2), data.get(5), entryCd))
                            .amount(Long.parseLong( (entryCd.equals("1"))? data.get(3) : data.get(4)) )
                            .remark(data.get(5))
                            .build();
                }).collect(Collectors.toList());

        return ImmutableMap.of(AccConstant.CM_RESULT, statementList);
    }

    private String getUsageWithType(String type, String remark, String entry) {
        String usageCd = "FF";

        if("이자".equals(type)) {
            return "99"; // 기타소득
        }

        if("급여".equals(type)) {
            return "10"; // 월급
        }

        if("타행MB".equals(type) && "박준서".equals(remark)) {
            return "00"; // 통장이행
        }

        return usageCd;
    }
}

package com.aibees.service.maria.account.utils.handler;

import com.aibees.service.maria.account.domain.entity.bank.BankStatementTmp;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.google.common.collect.ImmutableMap;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelParserForHANABANK implements ExcelParser {
    @Override
    public Map<String, Object> parse(Workbook workbook, String fileHash) {
        int HEADER_ROW = 5;
        int DATA_ROW = 6;

        Sheet dataSheet = workbook.getSheetAt(0);
        List<String> colList = ExcelParseHandler.getHeaderList(dataSheet.getRow(HEADER_ROW));
        Map<Integer, List<String>> dataListByRow = ExcelParseHandler.getDataList(dataSheet, colList, DATA_ROW);

        System.out.println("HANABANK Parsing is Completed,,, size : " + dataListByRow.size());
        String bankAcct = dataSheet.getRow(2).getCell(1).toString().replace("-", "");

        List<BankStatementTmp> statementList = dataListByRow.keySet().stream()
            .map(k -> {
                List<String> data = dataListByRow.get(k);
                String[] dateData = data.get(0).split(AccConstant.SPACE_STR);

                // 수입 : 0 / 지출 : 1
                String entry = (data.get(3).equals("0"))? "0": "1"; // 출금 쪽이 0이면 수입(0), 아니면 지출(1)

                return BankStatementTmp.builder()
                    .fileHash(fileHash)
                    .ymd(dateData[0].replace("-", ""))
                    .times(dateData[1].replace(":", ""))
                    .entryCd(entry)
                    .usageCd(getUsageWithType(data.get(1), data.get(2), entry))
                    .amount(Long.parseLong( (entry.equals("1"))? data.get(3) : data.get(4)) )
                    .remark(data.get(2))
                    .build();
            })
            .collect(Collectors.toList());


        return ImmutableMap.of(AccConstant.CM_RESULT, statementList);
    }

    private String getUsageWithType(String type, String remark, String entry) {
        String usageCd = "FF";

        if("공공요금".equals(type) || "대출이자".equals(type)) {
            return "03"; // 공과금
        }

        if(remark.contains("급여") && "타행이체".equals(type)) {
            return "10"; // 월급
        }

        if("타행송금".equals(type) || "대체".equals(type) || "타행이체".equals(type)) {
            if("박준서".equals(remark)) {
                return "00"; // 통장이행
            } else {
                return "01"; // 계좌이체
            }
        }
        if("통신요금".equals(type)) {
            return "0B"; // 통신비
        }

        if("예금이자".equals(type)) {
            return "99"; // 기타소득
        }

        return usageCd;
    }
}

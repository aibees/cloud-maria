package com.aibees.service.maria.account.utils.handler;

import com.aibees.service.maria.account.domain.entity.account.ImportStatementTmp;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.common.utils.MapUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ExcelParserForHANACARD implements ExcelParser {
    @Override
    public List<ImportStatementTmp> parse(Workbook workbook, Map<String, Object> param) {
        int HEADER_ROW = 3;
        int DATA_ROW = 4;

        Sheet dataSheet = workbook.getSheetAt(0);
        List<String> colList = ExcelParseHandler.getHeaderList(dataSheet.getRow(HEADER_ROW));
        Map<Integer, List<String>> dataListByRow = ExcelParseHandler.getDataList(dataSheet, colList, DATA_ROW);

        System.out.println("HYUNDAI Parsing is Completed,,, size : " + dataListByRow.size());

        return dataListByRow.keySet().stream()
                .map(k -> {
                    List<String> data = dataListByRow.get(k);

                    return ImportStatementTmp.builder()
                            .fileHash(MapUtils.getString(param, "fileHash"))
                            .ymd(data.get(0).replace(".", ""))
                            .times(data.get(1).replace(":", ""))
                            .cardNo(data.get(2).split(AccConstant.SPACE_STR)[1])
                            .approvNum(data.get(3))
                            .remark(data.get(4))
                            .amount(Long.parseLong(data.get(5).split("\\.")[0]))
                            .apYn(data.get(9))
                            .status(data.get(13))
                            .acctCd("FF")
                            .build();
                })
                .filter(state -> state.getApYn().equals("매입") || state.getStatus().equals("정상"))
                .collect(Collectors.toList());
    }
}

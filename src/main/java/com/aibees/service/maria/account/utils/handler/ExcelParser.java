package com.aibees.service.maria.account.utils.handler;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

public interface ExcelParser {
    Map<String, Object> parse(Workbook workbook, String fileHash);
}

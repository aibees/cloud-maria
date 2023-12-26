package com.aibees.service.maria.accountbook.domain;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface DataInfo {
    void excelParse(MultipartFile file) throws Exception;
    void textParse(String type, String smsText) throws Exception;

    void prepareStatementListByCondition(Map<String, Object> param) throws Exception;
    void prepareStatementTmpByFileId(String fileId) throws Exception;
}

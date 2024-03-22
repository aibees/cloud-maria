package com.aibees.service.maria.accountbook.service;

import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.common.vo.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public abstract class CommonAccountService {

    public ResponseEntity<ResponseData> successResponse(Object data) {
        return ResponseEntity.ok(
                ResponseData.builder()
                .data(data)
                .message(AccConstant.CM_SUCCESS)
                .build()
        );
    }

    public ResponseEntity<ResponseData> failedResponse(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseData.builder()
                        .message(AccConstant.CM_FAILED)
                        .data(e.getMessage())
                        .build()
        );
    }
}
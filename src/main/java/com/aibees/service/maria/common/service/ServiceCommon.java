package com.aibees.service.maria.common.service;

import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.common.StringUtils;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class ServiceCommon {

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

    protected String createFileNameHash() {
        return LocalDateTime
                .now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                .concat(StringUtils.getRandomStr(4));
    }
}

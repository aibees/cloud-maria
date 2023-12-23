package com.aibees.service.maria.accountbook.domain;

import com.aibees.service.maria.common.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractDataInfo {

    protected String createFileNameHash() {
        return LocalDateTime
                .now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                .concat(StringUtils.getRandomStr(4));
    }
}

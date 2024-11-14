package com.aibees.service.maria.common.enums;

public enum TokenType {
    ACCESS("User Access Token"),
    REFRESH("User Refresh Token");

    private final String desc;

    TokenType(String desc) {
        this.desc = desc;
    }
}

package com.aibees.service.maria.common.domain.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Setting Header 응답객체
 * 사용처 : SettingService
 */
@Data
@Builder
@ToString
public class SettingHeaderResp {
    private Long id;
    private String mainCategory;
    private String subCategory;
    private String code;
    private String desc;
    private String attributeNm01;
    private String attributeNm02;
    private String attributeNm03;
    private String attributeNm04;
    private String attributeNm05;
}
package com.aibees.service.maria.account.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccountSettingRes {

    private long headerId;
    private String mainCategory;
    private String subCategory;
    private String hCode;
    private String headerDesc;
    private String dCode;
    private String name;
    private int sort;
    private String enabledFlag;
    private String attributeNm01;
    private String attribute01;
    private String attributeNm02;
    private String attribute02;
    private String attributeNm03;
    private String attribute03;
    private String attributeNm04;
    private String attribute04;
    private String attributeNm05;
    private String attribute05;
}

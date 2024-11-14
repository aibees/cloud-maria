package com.aibees.service.maria.common.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SettingHeaderReq {
    private String mainCategory;
    private String subCategory;
    private String code;
}

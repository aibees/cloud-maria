package com.aibees.service.maria.account.domain.dto.account;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccountSettingReq {

    private String mainCategory;
    private String subCategory;
    private String hCode;
}

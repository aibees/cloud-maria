package com.aibees.service.maria.account.domain.entity.account.pk;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountSettingId {
    private long headerId;
    private String mainCategory;
    private String subCategory;
    private String code;
}

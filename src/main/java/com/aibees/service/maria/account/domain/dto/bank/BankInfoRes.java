package com.aibees.service.maria.account.domain.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BankInfoRes {
    private String bankId;
    private String bankAcct;
    private String bankCd;
    private String bankNm;
    private String bankType;
    private Long limitAmt;
    private String startDate;
    private String useYn;
}

package com.aibees.service.maria.account.domain.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BankInfoReq {
    private String bankId;
    private String bankCd;
    private String bankAcct;
    private String useYn;
    private String trxType;
    private Long limitAmt;
}

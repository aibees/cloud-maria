package com.aibees.service.maria.account.domain.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BankInfoReq {
    private String bankId;
    private String bankNm;
    private String bankCd;
    private String bankType;
    private String bankAcct;
    private String useYn;
    private String trxType;
    private Long limitAmt;
    private List<BankInfoReq> bankInfoReqs;
}

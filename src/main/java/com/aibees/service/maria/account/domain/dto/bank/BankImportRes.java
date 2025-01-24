package com.aibees.service.maria.account.domain.dto.bank;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BankImportRes {

    private String fileId;
    private String statementId;
    private String ymd;
    private String times;
    private String remark;
    private String acctCd;
    private long amount;
    private String fileHash;
    private String bankId;
    private String bankNm;
    private String entryCd;
}

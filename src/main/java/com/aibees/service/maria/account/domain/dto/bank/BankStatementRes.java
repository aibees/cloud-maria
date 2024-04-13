package com.aibees.service.maria.account.domain.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BankStatementRes {
    private String fileHash;
    private String ymd;
    private String times;
    private String bankId;
    private String entryCd;
    private String entryNm;
    private String bankNm;
    private String bankAcct;
    private String usageCd;
    private String usageNm;
    private String usageColor;
    private Long amount;
    private String remark;
    private String confirmStatus;
    private String wasteCheck;
}

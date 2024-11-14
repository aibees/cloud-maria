package com.aibees.service.maria.account.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AcctMasterReq {
    private String acctCd;
    private String acctNm;
    private String parentAcctCd;
    private String enabledFlag;
    private String finalFlag;
    private String acctType;
    private String searchTxt;
}

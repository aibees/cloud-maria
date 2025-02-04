package com.aibees.service.maria.account.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CashflowReq {
    private String acctCd;
    private String ym;
    private String acctType; // 수입 or 지출
}

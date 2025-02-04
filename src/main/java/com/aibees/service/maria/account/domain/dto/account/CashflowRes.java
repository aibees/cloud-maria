package com.aibees.service.maria.account.domain.dto.account;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CashflowRes {
    private String parentAcctCd;
    private String parentAcctNm;
    private String acctCd;
    private String acctNm;
    private String remark;
    private long crAmount;
    private long drAmount;
    private LocalDateTime transactionDate;
}
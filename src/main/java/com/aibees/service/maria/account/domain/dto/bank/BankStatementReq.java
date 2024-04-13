package com.aibees.service.maria.account.domain.dto.bank;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BankStatementReq {
    private String bankId;
    private String entryCd;
    private String ymdFrom;
    private String ymdTo;
    private String remark;
    private String usageCd;
    private Long amountFrom;
    private Long amountTo;
}

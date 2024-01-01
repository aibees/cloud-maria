package com.aibees.service.maria.accountbook.entity.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class BankInfoStatement {
    private String bankId;
    private String bankAcct;
    private String bankCd;
    private String bankNm;
    private String bankType;
    private long limitAmt;
}

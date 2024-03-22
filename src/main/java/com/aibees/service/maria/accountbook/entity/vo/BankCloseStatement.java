package com.aibees.service.maria.accountbook.entity.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class BankCloseStatement {
    private String bankId;
    private String ym;
    private long lastAmount;
    private long profitAmount;
    private long lossAmount;
    private long incomeAmount;
    private String beforeCloseYn;
    private String currCloseYn;
    private String closeYn;
    private String beforeYm;
    private String nextYm;
}

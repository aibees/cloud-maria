package com.aibees.service.maria.accountbook.entity.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class BankStatement {
    private String fileHash;
    private String ymd;
    private String times;
    private String bankId;
    private String bankNm;
    private String bankAcct;
    private String usageCd;
    private String usageNm;
    private String entryCd;
    private String entryNm;
    private String usageColor;
    private Long amount;
    private String remark;
}

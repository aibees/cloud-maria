package com.aibees.service.maria.accountbook.domain.bank;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class BankStatement {
    private String ymd;
    private String times;
    private String bankId;
    private String entry;
    private String usageCd;
    private String remark;
    private long amount;
    private String confirmStatus;
    private String wasteCheck;
}

package com.aibees.service.maria.account.domain.entity.card;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class CardStatement {
    private String ymd;
    private String approvNum;
    private String cardNo;
    private String usageCd;
    private long amount;
    private String remark;
    private String times;
    private String settleChk;
    private String installment;
}

package com.aibees.service.maria.account.domain.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardStatementReq {
    private String ymd;
    private String times;
    private String approvNum;
    private String cardNo;
    private String cardNm;
    private String usageCd;
    private String usageNm;
    private String usageColor;
    private Long amount;
    private String remark;
    private String apYn;
    private String status;

    // exceltmp에서 사용
    private String fileHash;
}

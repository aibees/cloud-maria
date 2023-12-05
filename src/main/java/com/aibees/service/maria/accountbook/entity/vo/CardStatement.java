package com.aibees.service.maria.accountbook.entity.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class CardStatement {
    private String ymd;
    private String times;
    private String approvNum;
    private String cardNo;
    private String cardNm;
    private String usageCd;
    private String usageNm;
    private String usageColor;
    private Integer amount;
    private String remark;
    private String apYn;
    private String status;

    // exceltmp에서 사용
    private String fileHash;
}

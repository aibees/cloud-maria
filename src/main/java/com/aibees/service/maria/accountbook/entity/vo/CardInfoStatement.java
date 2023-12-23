package com.aibees.service.maria.accountbook.entity.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class CardInfoStatement {
    private String cardNo;
    private String payway;
    private String cardName;
    private String bankCd;
    private String deadlineDate;
    private String paymentDate;
    private String expiredYm;
    private String creditYn;
    private String useYn;
    private String company;
    private Long limitAmt;
    private String selectedMain;
}

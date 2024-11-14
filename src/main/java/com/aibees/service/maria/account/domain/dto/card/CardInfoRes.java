package com.aibees.service.maria.account.domain.dto.card;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CardInfoRes {
    private String cardNo;
    private String cardComp;
    private String cardName;
    private String bankCd;
    private String deadlineDate;
    private String paymentDate;
    private String expiredYm;
    private String creditYn;
    private String useYn;
    private String company;
    private Long limitAmt;
    private String exposeMain;
}

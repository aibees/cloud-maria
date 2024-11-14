package com.aibees.service.maria.account.domain.dto.card;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CardInfoReq {
    // search variable
    private String cardName;
    private String useYn;
    private String expireYn;
    private String connBankCd;

    // insert variable
    private String status;
//    private String cardName;
    private String cardNo;
    private String cardComp;
    private String company;
    private String bankCd;
    private String expiredYm;
    private long limitAmt;
    private String creditYn;
    private String deadlineDate;
    private String exposeMain;
    private String paymentDate;
    private String trxType;
//    private String useYn;
    private List<CardInfoReq> cardInfoReqs;
}

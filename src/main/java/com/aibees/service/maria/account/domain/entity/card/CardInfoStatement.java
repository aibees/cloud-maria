package com.aibees.service.maria.account.domain.entity.card;

import com.aibees.service.maria.account.domain.entity.bank.pk.BankStatementId;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name="account_bank_statement")
@IdClass(BankStatementId.class)
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

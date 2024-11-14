package com.aibees.service.maria.account.domain.entity.card;

import com.aibees.service.maria.account.domain.entity.card.pk.AccountCardInfoPk;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name="account_card_info")
@IdClass(AccountCardInfoPk.class)
public class AccountCardInfo {
    @Id
    private String cardNo;
    @Id
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

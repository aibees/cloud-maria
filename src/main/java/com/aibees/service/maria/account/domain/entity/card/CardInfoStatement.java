package com.aibees.service.maria.account.domain.entity.card;

import com.aibees.service.maria.account.domain.entity.card.pk.CardInfoStatementId;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name="account_card_info")
@IdClass(CardInfoStatementId.class)
public class CardInfoStatement {
    @Id
    private String cardNo;
    @Id
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

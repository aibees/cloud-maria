package com.aibees.service.maria.account.domain.entity.card;

import com.aibees.service.maria.account.domain.entity.card.pk.CardStatementId;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name="account_card_statement")
@IdClass(CardStatementId.class)
public class CardStatement {
    @Id
    private String ymd;
    @Id
    private String approvNum;
    @Id
    private String cardNo;
    private String usageCd;
    private long amount;
    private String remark;
    private String times;
    private String settleChk;
    private String installment;
}

package com.aibees.service.maria.account.domain.entity.card;

import com.aibees.service.maria.account.domain.entity.card.pk.CardStatementTmpId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="account_card_statement_exceltmp")
@IdClass(CardStatementTmpId.class)
public class CardStatementTmp {
    @Id
    @Column(name="excelfile_id")
    private String fileHash;
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
    private String apYn;
    private String status;
}

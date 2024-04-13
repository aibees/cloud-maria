package com.aibees.service.maria.account.domain.entity.bank;

import com.aibees.service.maria.account.domain.entity.bank.pk.BankStatementId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name="account_bank_statement")
@IdClass(BankStatementId.class)
public class BankStatement {
    @Id
    private String ymd;
    @Id
    private String times;
    @Id
    private String bankId;
    @Id
    @Column(name="entry")
    private String entryCd;
    private String usageCd;
    private Long amount;
    private String remark;
    private String confirmStatus;
    private String wasteCheck;
}

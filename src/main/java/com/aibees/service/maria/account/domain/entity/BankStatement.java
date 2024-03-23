package com.aibees.service.maria.account.domain.entity;

import com.aibees.service.maria.account.domain.entity.pk.BankStatementId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="account_bank_statement")
@IdClass(BankStatementId.class)
public class BankStatement {
    private String fileHash;
    @Id
    private String ymd;
    @Id
    private String times;
    @Id
    private String bankId;
    @Id
    @Column(name="entry")
    private String entryCd;
    private String bankNm;
    private String bankAcct;
    private String usageCd;
    private String usageNm;
    private String entryNm;
    private String usageColor;
    private Long amount;
    private String remark;
    private String confirmStatus;
    private String wasteCheck;
}

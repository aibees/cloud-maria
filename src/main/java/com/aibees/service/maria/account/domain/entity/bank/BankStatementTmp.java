package com.aibees.service.maria.account.domain.entity.bank;

import com.aibees.service.maria.account.domain.entity.bank.pk.BankStatementTmpId;
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
@IdClass(BankStatementTmpId.class)
public class BankStatementTmp {
    @Id
    @Column(name="excelfile_id")
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
    private String usageCd;
    private Long amount;
    private String remark;
}

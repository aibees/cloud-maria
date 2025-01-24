package com.aibees.service.maria.account.domain.entity.account;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name="account_statement_exceltmp")
public class ImportStatementTmp {
    // common
    @Id
    private String statementId;
    private String ymd;
    private String times;
    private String remark;
    private String acctCd;
    private long amount;
    private String fileHash;

    // bank
    private String bankId;
    private String entryCd;

    // card
    private String cardNo;
    private String approvNum;
    private String apYn;
    private String status;
}

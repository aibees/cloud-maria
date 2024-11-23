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
@Builder
@Entity
@Table(name = "account_acct_master")
public class AcctMaster {
    @Id
    private String acctCd;
    private String acctNm;
    private String parentAcctCd;
    private String enabledFlag;
    private String finalFlag;
    private String acctType;
    private String additionalFlag;
}

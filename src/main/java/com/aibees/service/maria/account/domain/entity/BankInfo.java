package com.aibees.service.maria.account.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="account_bank_info")
public class BankInfo {
    @Id
    private String bankId;
    private String bankAcct;
    private String bankCd;
    private String bankNm;
    private String bankType;
    private long limitAmt;
    private String startDate;
}

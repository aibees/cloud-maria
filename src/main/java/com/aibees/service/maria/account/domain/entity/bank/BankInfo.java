package com.aibees.service.maria.account.domain.entity.bank;

import lombok.*;

import javax.persistence.*;

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
    private String useYn;
}

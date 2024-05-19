package com.aibees.service.maria.account.domain.entity.bank;

import lombok.*;

import javax.annotation.Nullable;
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
    @Nullable
    private Long limitAmt;
    private String startDate;
    private String useYn;
}

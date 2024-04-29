package com.aibees.service.maria.account.domain.entity.bank;

import com.aibees.service.maria.account.domain.entity.bank.pk.BankCloseId;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="account_bank_closing")
@IdClass(BankCloseId.class)
public class BankCloseStatement {
    @Id
    private String bankId;
    @Id
    private String ym;
    private long lastAmount;
    private long profitAmount;
    private long lossAmount;
    private long incomeAmount;
    private String beforeCloseYn;
    private String currCloseYn;
    private String closeYn;
    private String beforeYm;
    private String nextYm;
}

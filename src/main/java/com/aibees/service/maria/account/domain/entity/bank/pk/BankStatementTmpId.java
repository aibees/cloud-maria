package com.aibees.service.maria.account.domain.entity.bank.pk;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BankStatementTmpId implements Serializable {
    private String fileHash;
    private String ymd;
    private String times;
    private String bankId;
    private String entryCd;
}

package com.aibees.service.maria.account.domain.entity.card.pk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CardStatementTmpId implements Serializable {
    private String fileHash;
    private String ymd;
    private String approvNum;
    private String cardNo;
}

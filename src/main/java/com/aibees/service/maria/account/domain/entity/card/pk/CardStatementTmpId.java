package com.aibees.service.maria.account.domain.entity.card.pk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CardStatementTmpId {
    private String fileHash;
    private String ymd;
    private String approvNum;
    private String cardNo;
}

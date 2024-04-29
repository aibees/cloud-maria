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
public class CardInfoStatementId implements Serializable {
    private String cardNo;
    private String payway;
}

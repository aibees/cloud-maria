package com.aibees.service.maria.accountbook.entity.vo.pk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BankStatementId {
    private String ymd;
    private String times;
    private String bankId;
    private String entryCd;
}

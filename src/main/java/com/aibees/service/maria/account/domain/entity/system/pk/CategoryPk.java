package com.aibees.service.maria.account.domain.entity.system.pk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPk implements Serializable {
    private String sourceCd;
    private String categoryCd;
}

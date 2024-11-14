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
public class PresetDetailPk implements Serializable {
    private String presetCd;
    private long lineNo;
}

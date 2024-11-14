package com.aibees.service.maria.account.domain.entity.system;

import com.aibees.service.maria.account.domain.entity.system.pk.PresetDetailPk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "ma_preset_detail")
@IdClass(PresetDetailPk.class)
public class PresetDetail {
    @Id
    private String presetCd;
    @Id
    private long lineNo;
    private String acctCd;
    private String usageCd;
    private String dcFlag;
}

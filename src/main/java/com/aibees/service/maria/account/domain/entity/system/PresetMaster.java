package com.aibees.service.maria.account.domain.entity.system;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "ma_preset_master")
public class PresetMaster {
    @Id
    private String presetCd;
    private String presetNm;
    private String enabledFlag;
    private String description;
}

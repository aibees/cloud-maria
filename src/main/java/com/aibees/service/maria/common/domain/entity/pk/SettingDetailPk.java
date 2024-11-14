package com.aibees.service.maria.common.domain.entity.pk;

import lombok.*;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingDetailPk implements Serializable {
    private Long headerId;
    private String code;
}

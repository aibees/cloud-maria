package com.aibees.service.maria.common.domain.entity.pk;

import lombok.*;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonCounterPk implements Serializable {
    private String division;
    private String cntDate;
}

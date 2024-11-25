package com.aibees.service.maria.common.domain.entity.pk;

import lombok.*;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthPk implements Serializable {
    private Long uuid;
    private String authCd;
}

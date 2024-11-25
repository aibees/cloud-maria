package com.aibees.service.maria.common.domain.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserRole {
    private String authCd;
    private String authNm;
}

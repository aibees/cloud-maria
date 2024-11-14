package com.aibees.service.maria.account.domain.dto.system;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CategoryRes {
    private String sourceCd;
    private String categoryCd;
    private String categoryNm;
    private String enabledFlag;
}

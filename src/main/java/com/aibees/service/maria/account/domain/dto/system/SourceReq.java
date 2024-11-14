package com.aibees.service.maria.account.domain.dto.system;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SourceReq {
    private String sourceCd;
    private String sourceNm;
    private String enabledFlag;
    private String attribute1;
    private String attribute2;
    private String attribute3;
}

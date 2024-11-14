package com.aibees.service.maria.account.domain.dto.system;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PresetMasterReq {
    private String searchType;
    private String presetCd;
    private String presetNm;
    private String enabledFlag;
    private String description;
}

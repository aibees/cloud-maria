package com.aibees.service.maria.account.domain.mapper;

import com.aibees.service.maria.account.domain.dto.system.PresetDetailReq;
import com.aibees.service.maria.account.domain.dto.system.PresetDetailRes;
import com.aibees.service.maria.account.domain.dto.system.PresetMasterReq;
import com.aibees.service.maria.account.domain.dto.system.PresetMasterRes;
import com.aibees.service.maria.account.domain.entity.system.PresetDetail;
import com.aibees.service.maria.account.domain.entity.system.PresetMaster;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountPresetMapper {

    PresetMasterRes toMasterResp(PresetMaster entity);
    PresetMaster toMasterEntity(PresetMasterReq param);
    PresetDetailRes toDetailResp(PresetDetail entity);
    PresetDetail toDetailEntity(PresetDetailReq param);
}

package com.aibees.service.maria.multipart;

import com.aibees.service.maria.common.domain.entity.SettingHeader;
import com.aibees.service.maria.common.domain.vo.SettingHeaderResp;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SettingHeaderMapper {
    SettingHeaderResp toResp(SettingHeader entity);
}

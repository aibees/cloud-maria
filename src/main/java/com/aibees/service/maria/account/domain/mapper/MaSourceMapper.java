package com.aibees.service.maria.account.domain.mapper;

import com.aibees.service.maria.account.domain.dto.system.SourceRes;
import com.aibees.service.maria.account.domain.entity.system.Source;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MaSourceMapper {
    SourceRes toResp(Source entity);
}

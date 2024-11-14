package com.aibees.service.maria.account.domain.mapper;

import com.aibees.service.maria.account.domain.dto.account.AcctMasterRes;
import com.aibees.service.maria.account.domain.entity.account.AcctMaster;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountAcctMasterMapper {
    AcctMasterRes toResp(AcctMaster entity);
}

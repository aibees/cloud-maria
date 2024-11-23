package com.aibees.service.maria.account.domain.mapper;


import com.aibees.service.maria.account.domain.dto.bank.BankInfoRes;
import com.aibees.service.maria.account.domain.entity.bank.BankInfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BankInfoMapper {
    BankInfoRes toResp(BankInfo entity);
}

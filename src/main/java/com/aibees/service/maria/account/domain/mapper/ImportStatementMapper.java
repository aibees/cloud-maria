package com.aibees.service.maria.account.domain.mapper;

import com.aibees.service.maria.account.domain.dto.bank.BankImportRes;
import com.aibees.service.maria.account.domain.entity.account.ImportStatementTmp;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImportStatementMapper {

    BankImportRes toBankResp(ImportStatementTmp entity);
}

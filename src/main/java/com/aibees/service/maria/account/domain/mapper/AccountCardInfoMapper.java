package com.aibees.service.maria.account.domain.mapper;

import com.aibees.service.maria.account.domain.dto.card.CardInfoReq;
import com.aibees.service.maria.account.domain.dto.card.CardInfoRes;
import com.aibees.service.maria.account.domain.entity.card.AccountCardInfo;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountCardInfoMapper {
    CardInfoRes toResp(AccountCardInfo entity);
    AccountCardInfo toEntity(CardInfoReq param);
}

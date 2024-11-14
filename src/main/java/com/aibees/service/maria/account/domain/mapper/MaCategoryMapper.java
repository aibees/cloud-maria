package com.aibees.service.maria.account.domain.mapper;


import com.aibees.service.maria.account.domain.dto.system.CategoryRes;
import com.aibees.service.maria.account.domain.entity.system.Category;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MaCategoryMapper {
    CategoryRes toResp(Category entity);
}

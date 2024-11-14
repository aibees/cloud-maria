package com.aibees.service.maria.common.service;

import com.aibees.service.maria.common.domain.dto.SettingHeaderReq;
import com.aibees.service.maria.common.domain.entity.SettingHeader;
import com.aibees.service.maria.common.domain.repo.SettingHeaderRepo;
import com.aibees.service.maria.common.domain.vo.SettingHeaderResp;
import com.aibees.service.maria.multipart.SettingHeaderMapper;
import lombok.AllArgsConstructor;

import java.util.Objects;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommonSettingService {

    private final SettingHeaderRepo headerRepo;
    private final SettingHeaderMapper headerMapper;

    public SettingHeaderResp findSettingHeaderOne(SettingHeaderReq param) {
        SettingHeader headerOne = headerRepo.findByMainCategoryAndSubCategoryAndCode(
            param.getMainCategory(), param.getSubCategory(), param.getCode()
        ).orElse(null);

        if (Objects.isNull(headerOne)) {
            return null;
        } else {
            return headerMapper.toResp(headerOne);
        }
    }
}

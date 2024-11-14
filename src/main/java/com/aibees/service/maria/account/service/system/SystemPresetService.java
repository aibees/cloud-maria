package com.aibees.service.maria.account.service.system;

import com.aibees.service.maria.account.domain.dto.system.PresetMasterReq;
import com.aibees.service.maria.account.domain.dto.system.PresetMasterRes;
import com.aibees.service.maria.account.domain.mapper.AccountPresetMapper;
import com.aibees.service.maria.account.domain.repo.system.SystemPresetRepo;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.ServiceCommon;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SystemPresetService extends ServiceCommon {

    private final SystemPresetRepo presetRepo;
    private final AccountPresetMapper presetMapper;

    public ResponseEntity<ResponseData> getPresetMasterList(PresetMasterReq param) {

        List<PresetMasterRes> resList = presetRepo.getPresetMasterByCondition(param)
            .stream()
            .map(presetMapper::toMasterResp)
            .collect(Collectors.toList());

        return successResponse(resList);
    }
}

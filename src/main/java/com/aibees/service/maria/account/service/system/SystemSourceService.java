package com.aibees.service.maria.account.service.system;

import com.aibees.service.maria.account.domain.dto.system.SourceRes;
import com.aibees.service.maria.account.domain.mapper.MaSourceMapper;
import com.aibees.service.maria.account.domain.repo.system.SystemSourceRepo;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.excepts.MariaException;
import com.aibees.service.maria.common.service.ServiceCommon;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SystemSourceService extends ServiceCommon {

    private final SystemSourceRepo sourceRepo;
    private final MaSourceMapper sourceMapper;

    public List<SourceRes> getServiceCodeList() {
        try {
            List<SourceRes> result = sourceRepo.findAll()
                .stream()
                .map(sourceMapper::toResp)
                .collect(Collectors.toList());

            return result;
        } catch (Exception e) {
            throw new MariaException(e.getMessage());
        }
    }
}

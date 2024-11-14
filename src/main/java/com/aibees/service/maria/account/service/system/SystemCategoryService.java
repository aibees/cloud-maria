package com.aibees.service.maria.account.service.system;

import com.aibees.service.maria.account.domain.dto.system.CategoryReq;
import com.aibees.service.maria.account.domain.dto.system.CategoryRes;
import com.aibees.service.maria.account.domain.mapper.MaCategoryMapper;
import com.aibees.service.maria.account.domain.repo.system.SystemCategoryRepo;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.ServiceCommon;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SystemCategoryService extends ServiceCommon {

    private final SystemCategoryRepo categoryRepo;
    private final MaCategoryMapper categoryMapper;

    public ResponseEntity<ResponseData> getCategoryBySourceCd(CategoryReq param) {
        try {
            List<CategoryRes> result = categoryRepo.findAllBySourceCd(param.getSourceCd())
                .stream()
                .map(categoryMapper::toResp)
                .collect(Collectors.toList());


            return successResponse(result);
        } catch (Exception e) {
            return failedResponse(e);
        }
    }
}

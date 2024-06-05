package com.aibees.service.maria.account.service.account;

import com.aibees.service.maria.account.domain.dto.account.AccountSettingReq;
import com.aibees.service.maria.account.domain.dto.account.AccountSettingRes;
import com.aibees.service.maria.account.domain.repo.account.AccountSettingRepo;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.ServiceCommon;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SettingService extends ServiceCommon {

    private final AccountSettingRepo settingRepo;

    public List<AccountSettingRes> getSettingListByCond(AccountSettingReq param) {
        return settingRepo.getSettingDetails(param);
    }

    public ResponseEntity<ResponseData> getSettingListForCommon(AccountSettingReq param) {
        try {
            return successResponse(settingRepo.getSettingDetails(param));
        } catch (Exception e) {
            return failedResponse(e);
        }
    }
}

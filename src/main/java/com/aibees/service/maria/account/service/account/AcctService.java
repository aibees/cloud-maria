package com.aibees.service.maria.account.service.account;

import com.aibees.service.maria.account.domain.dto.account.AcctMasterReq;
import com.aibees.service.maria.account.domain.dto.account.AcctMasterRes;
import com.aibees.service.maria.account.domain.entity.account.AcctMaster;
import com.aibees.service.maria.account.domain.mapper.AccountAcctMasterMapper;
import com.aibees.service.maria.account.domain.repo.account.AcctMasterRepo;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.excepts.MariaException;
import com.aibees.service.maria.common.service.ServiceCommon;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AcctService extends ServiceCommon {

    private final AcctMasterRepo acctRepo;
    private final AccountAcctMasterMapper acctMapper;

    public List<AcctMasterRes> searchAcctListForPopup(AcctMasterReq param) {
        try {
            return acctRepo.findAllByAcctCdContainingOrAcctNmContaining(param.getSearchTxt(), param.getSearchTxt())
                .stream()
                .filter(data ->
                    StringUtils.equals(data.getFinalFlag(), AccConstant.YES)
                        && StringUtils.equals(data.getEnabledFlag(), AccConstant.YES)
                )
                .map(acctMapper::toResp)
                .collect(Collectors.toList());
        } catch(Exception e) {
            throw new MariaException(e.getMessage());
        }
    }

    public List<AcctMasterRes> getAcctMasterForSelect(AcctMasterReq param) {
        return acctRepo.findAllByEnabledFlagAndFinalFlag(param.getEnabledFlag(), param.getFinalFlag())
            .stream()
            .map(acctMapper::toResp)
            .collect(Collectors.toList());
    }
}

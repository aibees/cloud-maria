package com.aibees.service.maria.account.service.account;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aibees.service.maria.account.domain.dto.account.AcctMasterReq;
import com.aibees.service.maria.account.domain.dto.account.AcctMasterRes;
import com.aibees.service.maria.account.domain.entity.account.AcctMaster;
import com.aibees.service.maria.account.domain.mapper.AccountAcctMasterMapper;
import com.aibees.service.maria.account.domain.repo.account.AcctMasterRepo;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.common.excepts.MariaException;
import com.aibees.service.maria.common.service.ServiceCommon;
import com.aibees.service.maria.common.utils.StringUtils;
import lombok.AllArgsConstructor;

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
                    StringUtils.isEquals(data.getFinalFlag(), AccConstant.YES)
                        && StringUtils.isEquals(data.getEnabledFlag(), AccConstant.YES)
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

    /**
     * 계정과목관리 조회
     * @param type
     * @param param
     * @return
     */
    public List<AcctMasterRes> getAcctSettingList(String type, AcctMasterReq param) {
        if ("first".equals(type)) {
            return getFirstSetting(param);
        } else {
            return getOtherSetting(param);
        }
    }

    private List<AcctMasterRes> getFirstSetting(AcctMasterReq param) {
        return acctRepo.selectAcctMasterSetting(param);
    }

    private List<AcctMasterRes> getOtherSetting(AcctMasterReq param) {
        return acctRepo.selectAcctMasterSetting(param);
    }

    /**
     * 계정과목 INSERT / UPDATE
     * @param param
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAcctSetting(AcctMasterReq param) {
        String parentAcctCd = param.getData().get(0).getParentAcctCd();
        Optional<AcctMaster> parentOption = acctRepo.findById(parentAcctCd);

        if (parentOption.isEmpty()) {
            throw new MariaException("비정상적인 부모계정");
        }

        AcctMasterRes parent = acctMapper.toResp(parentOption.get());

        param.getData().forEach(data -> {
            if ("INSERT".equals(data.getTrxType())) {
                insertAcct(data, parent);
            } else if ("UPDATE".equals(data.getTrxType())) {
                updateAcct(data, parent);
            } else {

            }
        });
    }

    private void insertAcct(AcctMasterReq req, AcctMasterRes parent) {
        int splices = "SECOND".equals(req.getAcctLevel()) ? 1 : 3;

        if (!req.getAcctCd().startsWith(parent.getAcctCd().substring(0, splices))) {
            throw new MariaException("부모계정에 귀속되지 않는 계정코드");
        }

        if (StringUtils.isNull(req.getAcctNm())) {
            throw new MariaException("계정과목명은 필수");
        }

        AcctMaster newAcct = AcctMaster.builder()
            .acctCd(req.getAcctCd())
            .acctNm(req.getAcctNm())
            .parentAcctCd(parent.getAcctCd())
            .enabledFlag(AccConstant.YES)
            .finalFlag(req.getFinalFlag())
            .acctType(parent.getAcctType())
            .additionalFlag(parent.getAdditionalFlag())
            .build();

        acctRepo.save(newAcct);
    }

    private void updateAcct(AcctMasterReq req, AcctMasterRes parent) {
        AcctMaster acct = acctRepo.findById(req.getAcctCd()).orElseThrow(
            () -> new MariaException("존재하지 않는 계정코드")
        );

        acct.setAcctNm(req.getAcctNm());
    }
}

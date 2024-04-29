package com.aibees.service.maria.account.service.bank;

import com.aibees.service.maria.account.domain.dto.bank.BankInfoReq;
import com.aibees.service.maria.account.domain.entity.bank.BankInfo;
import com.aibees.service.maria.account.domain.repo.bank.BankInfoRepo;
import com.aibees.service.maria.account.service.AccountServiceCommon;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.account.utils.enums.TrxType;
import com.aibees.service.maria.common.StringUtils;
import com.aibees.service.maria.common.vo.ResponseData;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BankInfoService extends AccountServiceCommon {

    private final BankInfoRepo bankInfoRepo;

    public ResponseEntity<ResponseData> getBankInfoList(BankInfoReq param) {
        // TODO : validation
        try {

            List<BankInfo> bankInfos = bankInfoRepo.findAllByBankNmContainingAndBankAcctContainingAndUseYn(
                    param.getBankNm(),
                    param.getBankAcct(),
                    param.getUseYn()
            );

            return successResponse(bankInfos);
        } catch(Exception e) {
            return failedResponse(e);
        }
    }

    public ResponseEntity<ResponseData> saveBankInfoList(BankInfoReq param) {
        try {
            BankInfo saved = null;
            if (StringUtils.isEquals(param.getTrxType(), TrxType.INSERT.name())) {
                BankInfo newInfo = BankInfo.builder().build();
                saved = bankInfoRepo.save(newInfo);

            } else if (StringUtils.isEquals(param.getTrxType(), TrxType.UPDATE.name())) {
                BankInfo existed = bankInfoRepo.findByBankIdAndBankCd(param.getBankId(), param.getBankCd()).orElseThrow();
                updateBankInfo(existed, param);
                saved = existed;
            } else {
                // DELETE
                BankInfo existed = bankInfoRepo.findByBankIdAndBankCd(param.getBankId(), param.getBankCd()).orElseThrow();
                bankInfoRepo.delete(existed);
            }

            return successResponse(ImmutableMap.of(
                    AccConstant.CM_RESULT, String.join("_", param.getTrxType(), AccConstant.CM_SUCCESS),
                    AccConstant.CM_DATA, saved
            ));
        } catch(Exception e) {
            return failedResponse(e);
        }
    }

    private void updateBankInfo(BankInfo entity, BankInfoReq param) {
        entity.setBankAcct(param.getBankAcct());
       // entity.setBankNm(param.getBankNm());
        entity.setLimitAmt(param.getLimitAmt());
        entity.setUseYn(param.getUseYn());
    }
}

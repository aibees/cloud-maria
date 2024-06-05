package com.aibees.service.maria.account.service.bank;

import com.aibees.service.maria.account.domain.dto.bank.BankInfoReq;
import com.aibees.service.maria.account.domain.entity.bank.BankInfo;
import com.aibees.service.maria.account.domain.repo.bank.BankInfoRepo;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.account.utils.enums.TrxType;
import com.aibees.service.maria.common.StringUtils;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.ServiceCommon;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BankInfoService extends ServiceCommon {

    private final BankInfoRepo bankInfoRepo;

    public ResponseEntity<ResponseData> getBankInfoList(BankInfoReq param) {
        // TODO : validation
        try {

            List<BankInfo> bankInfos = bankInfoRepo.getBankInfoListByCondition(param)
                    .stream()
                    .peek(bank -> {
                        Long limitAmt = bank.getLimitAmt();
                        bank.setLimitAmt(limitAmt == null ? -1L : limitAmt);
                        bank.setUseYn(StringUtils.isNull(bank.getUseYn()) ? AccConstant.No : bank.getUseYn());
                    })
                    .collect(Collectors.toList());

            return successResponse(bankInfos);
        } catch(Exception e) {
            return failedResponse(e);
        }
    }

    public List<BankInfo> getBankInfoByCond(BankInfoReq param) {
        return bankInfoRepo.getBankInfoListByCondition(param);
    }

    /**
     * select option을 위한 은행 별 선택지 만들기
     * aggregate에서 setting_code와 Name 매핑
     * @return
     */
    public List<String> getBankSelectList(BankInfoReq param) {
        Set<String> bankInfos = bankInfoRepo.getBankInfoListByCondition(param)
            .stream()
            .collect(Collectors.groupingBy(BankInfo::getBankCd))
            .keySet();

        return List.copyOf(bankInfos);
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

package com.aibees.service.maria.account.service.bank;

import com.aibees.service.maria.account.domain.dto.bank.BankInfoReq;
import com.aibees.service.maria.account.domain.dto.bank.BankInfoRes;
import com.aibees.service.maria.account.domain.entity.bank.BankInfo;
import com.aibees.service.maria.account.domain.mapper.BankInfoMapper;
import com.aibees.service.maria.account.domain.repo.bank.BankInfoRepo;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.account.utils.enums.TrxType;
import com.aibees.service.maria.common.excepts.MariaException;
import com.aibees.service.maria.common.utils.StringUtils;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.ServiceCommon;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BankInfoService extends ServiceCommon {

    private final BankInfoRepo bankInfoRepo;
    private final BankInfoMapper bankInfoMapper;

    public List<BankInfoRes> getBankInfoList(BankInfoReq param) {
        // TODO : validation
        try {
            return bankInfoRepo.getBankInfoListByCondition(param)
                .stream()
                .peek(bank -> {
                    Long limitAmt = bank.getLimitAmt();
                    bank.setLimitAmt(limitAmt == null ? 0 : limitAmt);
                    bank.setUseYn(StringUtils.isNull(bank.getUseYn()) ? AccConstant.No : bank.getUseYn());
                    bank.setDisplayYn(StringUtils.isNull(bank.getDisplayYn()) ? AccConstant.No : bank.getDisplayYn());
                })
                .map(bankInfoMapper::toResp)
                .collect(Collectors.toList());

        } catch(Exception e) {
            throw new MariaException(e.getLocalizedMessage());
        }
    }

    public void saveBankInfo(List<BankInfoReq> saveParam) {

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

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveBankInfoList(List<BankInfoReq> param) {
        try {
            param.forEach(data -> {
                BankInfo bankInfo = bankInfoRepo.findById(data.getBankId())
                    .orElseThrow(() -> new MariaException("데이터 없음"));

                if (TrxType.INSERT.name().equals(data.getTrxType())) {
                    BankInfo newInfo = bankInfoMapper.toEntity(data);
                    bankInfoRepo.save(newInfo);

                } else if (TrxType.DELETE.name().equals(data.getTrxType())) {
                    bankInfoRepo.delete(bankInfo);

                } else {
                    // update
                    updateBankInfo(bankInfo, data);
                }
            });
        } catch(Exception e) {
            throw new MariaException(e.getMessage());
        }
    }

    private void updateBankInfo(BankInfo entity, BankInfoReq param) {
        entity.setBankAcct(param.getBankAcct());
        entity.setBankNm(param.getBankNm());
        entity.setLimitAmt(param.getLimitAmt());
        entity.setUseYn(param.getUseYn());
        entity.setDisplayYn(param.getDisplayYn());
    }
}

package com.aibees.service.maria.account.service.bank;

import com.aibees.service.maria.account.domain.dto.account.AccountSettingReq;
import com.aibees.service.maria.account.domain.dto.account.AccountSettingRes;
import com.aibees.service.maria.account.domain.dto.bank.BankInfoReq;
import com.aibees.service.maria.account.domain.dto.bank.BankStatementReq;
import com.aibees.service.maria.account.domain.dto.bank.BankStatementRes;
import com.aibees.service.maria.account.domain.entity.bank.BankStatement;
import com.aibees.service.maria.account.domain.repo.account.AccountSettingRepo;
import com.aibees.service.maria.account.service.AccountServiceCommon;
import com.aibees.service.maria.common.StringUtils;
import com.aibees.service.maria.common.vo.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BankAggregate extends AccountServiceCommon {

    private final BankService bankService;
    private final BankCloseService bankCloseService;
    private final BankInfoService bankInfoService;

    private final AccountSettingRepo settingRepo;

    public ResponseEntity<ResponseData> getBankStatementList(BankStatementReq param) {
        try {
            AccountSettingReq settingParam = new AccountSettingReq();
            settingParam.setMainCategory("ACCOUNT");
            settingParam.setSubCategory("COMBO");

            Map<String, List<AccountSettingRes>> settingMap = settingRepo.getSettingDetails(settingParam)
                    .stream().collect(Collectors.groupingBy(AccountSettingRes::getHeaderCode));

            List<AccountSettingRes> usageSettings = settingMap.get("USAGE");
            List<AccountSettingRes> entrySettings = settingMap.get("ENTRY");

            List<BankStatement> statements = bankService.getBankStatementList(param);

            List<BankStatementRes> resultList = statements.stream()
                .map(state -> {
                    try {
                        String entryNm = entrySettings.stream().filter(setting -> setting.getDetailCode().equals(state.getEntryCd())).findFirst().get().getName();
                        AccountSettingRes settingUsage = usageSettings.stream().filter(setting -> setting.getDetailCode().equals(state.getUsageCd())).findFirst().get();
                        String usageNm = settingUsage.getName();
                        String usageColor = settingUsage.getAttribute03();

                        return BankStatementRes
                                .builder()
                                .ymd(state.getYmd())
                                .times(state.getTimes())
                                .bankId(state.getBankId())
                                .entryCd(state.getEntryCd())
                                .entryNm(entryNm)
                                .usageCd(state.getUsageCd())
                                .usageNm(usageNm)
                                .usageColor(StringUtils.getWithDefault(usageColor, "#FFFFFF"))
                                .amount(state.getAmount())
                                .remark(state.getRemark())
                                .confirmStatus(state.getConfirmStatus())
                                .wasteCheck(state.getWasteCheck())
                                .build();
                    } catch(Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());

            return successResponse(resultList);
        } catch(Exception e) {
            return failedResponse(e);
        }
    }

    public ResponseEntity<ResponseData> getBankInfoList(BankInfoReq param) {
        try {
            List<>
            return successResponse(null);
        } catch(Exception e) {
            return failedResponse(e);
        }
    }

    public ResponseEntity<ResponseData> saveBankStatement(Map<String, Object> data) {
        return bankService.saveBankStatement();
    }

    public ResponseEntity<ResponseData>saveBankInfo(BankInfoReq param) {
        return null;
    }
}

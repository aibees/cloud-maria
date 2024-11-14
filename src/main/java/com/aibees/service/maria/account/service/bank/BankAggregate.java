package com.aibees.service.maria.account.service.bank;

import com.aibees.service.maria.account.domain.dto.account.AccountSettingReq;
import com.aibees.service.maria.account.domain.dto.account.AccountSettingRes;
import com.aibees.service.maria.account.domain.dto.bank.BankInfoReq;
import com.aibees.service.maria.account.domain.dto.bank.BankStatementReq;
import com.aibees.service.maria.account.domain.dto.bank.BankStatementRes;
import com.aibees.service.maria.account.domain.entity.bank.BankInfo;
import com.aibees.service.maria.account.domain.entity.bank.BankStatement;
import com.aibees.service.maria.account.domain.entity.bank.BankStatementTmp;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.common.utils.StringUtils;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.ServiceCommon;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BankAggregate extends ServiceCommon {

    private final BankService bankService;
    private final BankCloseService bankCloseService;
    private final BankInfoService bankInfoService;


    public ResponseEntity<ResponseData> getBankStatementList(BankStatementReq param) {
        try {
            AccountSettingReq settingParam = new AccountSettingReq();
            settingParam.setMainCategory("ACCOUNT");
            settingParam.setSubCategory("COMBO");

            settingParam.setHCode("USAGE");
            List<AccountSettingRes> usageSettings = null;// settingService.getSettingListByCond(settingParam);

            settingParam.setHCode("ENTRY");
            List<AccountSettingRes> entrySettings = null;// settingService.getSettingListByCond(settingParam);

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
        return bankInfoService.getBankInfoList(param);
    }

    public ResponseEntity<ResponseData> getBankStatementTmpList(String fileHashId) {
        List<BankStatementTmp> tmpList = bankService.getBankStatementTmpList(fileHashId);

        BankInfoReq req = BankInfoReq.builder().build();
        Map<String, String> bankInfos = bankInfoService.getBankInfoByCond(req)
                .stream()
                .collect(Collectors.groupingBy(BankInfo::getBankId, Collectors.mapping(BankInfo::getBankNm, Collectors.joining())));

        List<BankStatementRes> resultList = tmpList.stream()
                .map(tmp ->
                        BankStatementRes.builder()
                        .fileHash(tmp.getFileHash())
                        .bankId(tmp.getBankId())
                        .bankNm(bankInfos.get(tmp.getBankId()))
                        .entryCd(tmp.getEntryCd())
                        .entryNm(tmp.getEntryCd().equals("1") ? "지출" : "수입")
                        .usageCd(tmp.getUsageCd())
                        .ymd(tmp.getYmd())
                        .times(tmp.getTimes())
                        .remark(tmp.getRemark())
                        .amount(tmp.getAmount())
                        .build())
                .collect(Collectors.toList());

        return successResponse(resultList);
    }

    public ResponseEntity<ResponseData> getBankSelectList() {
        try {
            BankInfoReq infoReq = new BankInfoReq();
            infoReq.setUseYn(AccConstant.YES);
            infoReq.setBankType("입출금");
            List<String> bankSelectCd = bankInfoService.getBankSelectList(infoReq);

            AccountSettingReq settingParam = new AccountSettingReq();
            settingParam.setMainCategory("ACCOUNT");
            settingParam.setSubCategory("COMBO");
            settingParam.setHCode("BANK");
//            Map<String, String> bankSetting = settingService.getSettingListByCond(settingParam)
//                    .stream()
//                    .collect(Collectors.groupingBy(AccountSettingRes::getDetailCode, Collectors.mapping(AccountSettingRes::getName, Collectors.joining())));

            List<Map<String, String>> result = null; //bankSelectCd.stream()
//                    .map(code -> ImmutableMap.of("code", code, "name", bankSetting.get(code)
//                    )).collect(Collectors.toList());

            return successResponse(result);
        } catch(Exception e) {
            e.printStackTrace();
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

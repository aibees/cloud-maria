package com.aibees.service.maria.account.service.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aibees.service.maria.account.domain.dto.account.CashflowReq;
import com.aibees.service.maria.account.domain.dto.account.CashflowRes;
import com.aibees.service.maria.account.domain.dto.account.JournalLinesReq;
import com.aibees.service.maria.account.domain.entity.account.AcctMaster;
import com.aibees.service.maria.account.domain.repo.account.AcctMasterRepo;
import com.aibees.service.maria.account.domain.repo.account.JournalLinesRepo;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.common.utils.StringUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CashflowService {

    private final JournalLinesRepo lineRepo;
    private final AcctMasterRepo acctRepo;

    public List<CashflowRes> getCashflowData(CashflowReq param) {
        Map<String, String> acctMap = new HashMap<>();
        acctRepo.findAllByEnabledFlagAndFinalFlag(AccConstant.YES, AccConstant.No)
            .forEach(acc -> {
                String key = acc.getAcctCd();
                String parent = acc.getParentAcctCd();

                acctMap.put(key, parent);
            });

        List<String> acctList = new ArrayList<>();
        acctList.add("1010001"); // 당좌예금

        JournalLinesReq lineParam = JournalLinesReq.builder()
            .flowType(param.getAcctType())
            .ym(param.getYm())
            .acctCdList(acctList)
            .build();
        return extractJournal(lineParam, acctMap);
    }

    /**
     * 수익관련 전표 현금흐름 조회
     * @param param
     */
    private List<CashflowRes> extractJournal(JournalLinesReq lineParam, Map<String, String> acctMap) {

        Map<String, String> acctParentMap = new HashMap<>();
        List<AcctMaster> accts = acctRepo.findAllByEnabledFlagAndFinalFlag("Y", "N");
        accts.forEach(acct -> acctParentMap.put(acct.getAcctCd(), acct.getAcctNm()));

        List<CashflowRes> lines = lineRepo.selectCashflowByGrouping(lineParam);
        lines.forEach(resp -> resp.setParentAcctNm(acctParentMap.get(resp.getParentAcctCd())));

        return lines;
    }

    /**
     * 계정 별 디테일 조회
     * @param param
     * @return
     */
    public List<CashflowRes> getCashflowDataDetail(CashflowReq param) {
        String acctCd = param.getAcctCd();

        JournalLinesReq lineParam = JournalLinesReq.builder()
            .ym(param.getYm())
            .acctCd(acctCd)
            .build();

        return lineRepo.selectCashflowByAcctType(lineParam);
    }
}

package com.aibees.service.maria.account.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.aibees.service.maria.account.domain.dto.account.*;
import com.aibees.service.maria.account.domain.entity.account.AcctMaster;
import com.aibees.service.maria.account.service.account.AcctService;
import com.aibees.service.maria.account.service.account.CashflowService;
import com.aibees.service.maria.account.service.account.JournalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/account")
public class AccountController {

    private final AcctService acctService;
    private final JournalService journalService;
    private final CashflowService cashflowService;

    /***************************************************
     ********       계정과목 등 전표 공통값코드        ********
     ***************************************************/
    @GetMapping("/acct/popup")
    public List<AcctMasterRes> getAcctMasterList(AcctMasterReq param) {
        return acctService.searchAcctListForPopup(param);
    }

    @GetMapping("/acct")
    public List<AcctMasterRes> getAcctMasterListForSelect(AcctMasterReq param) {
        return acctService.getAcctMasterForSelect(param);
    }

    @GetMapping("/acct/{settingType}")
    public List<AcctMasterRes> getAcctSettingFirst(AcctMasterReq param) {
        return acctService.getAcctSettingList(param.getSettingType(), param);
    }

    @PostMapping("/acct/setting")
    public void updateAcctSetting(@RequestBody AcctMasterReq param) {
        acctService.updateAcctSetting(param);
    }

    /***************************************************
     ********              전표처리               ********
     ***************************************************/
    @GetMapping("/journal/{jeHeaderNo}")
    public JournalHeaderRes getJournalByJournalNo(@PathVariable(required = false) String jeHeaderNo) {
        JournalHeaderReq req = new JournalHeaderReq();
        req.setJeHeaderNo(jeHeaderNo);
        return journalService.getJournalHeaderById(req);
    }

    @PostMapping("/journal")
    public String saveJournal(@RequestBody JournalHeaderReq journal) {

        return journalService.saveJournal(journal);
    }

    /***************************************************
     ********              현금흐름표               ********
     ***************************************************/
    @GetMapping("/cashflow/{ym}/{acctType}")
    public List<CashflowRes> getCashflowData(CashflowReq param) {
        return cashflowService.getCashflowData(param);
    }

    @GetMapping("/cashflow/detail/{ym}/{acctCd}")
    public List<CashflowRes> getCashflowDataDetail(CashflowReq param) {
        return cashflowService.getCashflowDataDetail(param);
    }
}

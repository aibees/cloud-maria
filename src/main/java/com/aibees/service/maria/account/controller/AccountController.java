package com.aibees.service.maria.account.controller;

import java.util.List;

import com.aibees.service.maria.account.domain.dto.account.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aibees.service.maria.account.service.account.AcctService;
import com.aibees.service.maria.account.service.account.JournalService;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/account")
public class AccountController {

    private final AcctService acctService;
    private final JournalService journalService;
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
}

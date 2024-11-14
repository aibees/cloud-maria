package com.aibees.service.maria.account.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aibees.service.maria.account.domain.dto.account.AcctMasterReq;
import com.aibees.service.maria.account.domain.dto.account.JournalHeaderReq;
import com.aibees.service.maria.account.domain.dto.account.JournalLinesReq;
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
    public ResponseEntity<ResponseData> getAcctMasterList(AcctMasterReq param) {

        log.info(param.toString());

        return acctService.searchAcctListForPopup(param);
    }

    /***************************************************
     ********              전표처리               ********
     ***************************************************/
    @GetMapping("/journal/{jeHeaderNo}")
    public ResponseEntity<ResponseData> getJournalByJournalNo(@PathVariable(required = false) String jeHeaderNo) {
        JournalHeaderReq req = new JournalHeaderReq();
        req.setJeHeaderNo(jeHeaderNo);
        return journalService.getJournalHeaderById(req);
    }

    @PostMapping("/journal")
    public ResponseEntity<ResponseData> saveJournal(@RequestBody JournalHeaderReq journal) {

        return journalService.saveJournal(journal);
    }
}

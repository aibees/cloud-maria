package com.aibees.service.maria.account.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.aibees.service.maria.account.domain.dto.account.ImportFileRes;
import com.aibees.service.maria.account.domain.dto.bank.BankImportReq;
import com.aibees.service.maria.account.domain.dto.bank.BankImportRes;
import com.aibees.service.maria.account.domain.dto.bank.BankInfoReq;
import com.aibees.service.maria.account.domain.dto.bank.BankInfoRes;
import com.aibees.service.maria.account.service.bank.BankAggregate;
import com.aibees.service.maria.account.service.bank.BankImportService;
import com.aibees.service.maria.account.service.bank.BankInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@RequestMapping("/account/bank")
@Slf4j
public class BankController {

    private final BankAggregate bankAggregate;
    private final BankInfoService bankInfoService;
    private final BankImportService importService;

    /***************************************************
     ********       Bank - tmp Statement        ********
     ***************************************************/

    @GetMapping("/files")
    public List<ImportFileRes> getBankExcelTmpFiles() {
        return importService.getTmpFileList();
    }

    @GetMapping("/files/{fileHash}")
    public List<BankImportRes> getBankTmpListByFile(BankImportReq param) {
        return importService.getTmpStatementList(param);
    }

    @PostMapping("/files")
    public BankImportRes uploadBankExcelFile(BankImportReq param) {
        return importService.uploadBankTransaction(param);
    }

    @PutMapping("/files")
    public BankImportRes saveTmpStatement(@RequestBody BankImportReq param) {
        return importService.tmpSaveStatement(param);
    }

    @PostMapping("/files/je")
    public BankImportRes createJe(@RequestBody BankImportReq param) {
        return importService.createJeLines(param);
    }

    /***************************************************
     ********        Bank - Account Info        ********
     ***************************************************/

    /**
     * Bank Info 리스트 조회
     * @param param
     * @return
     */
    @GetMapping("/infos")
    public List<BankInfoRes> getBankInfoList(BankInfoReq param) {
        return bankInfoService.getBankInfoList(param);
    }

    /**
     * 계좌정보 마스터는 단건 별 save처리
     * @param param
     * @return
     */
    @PostMapping("/infos")
    public void saveBankInfo(@RequestBody BankInfoReq param) {
        bankInfoService.saveBankInfoList(param.getBankInfoReqs());
    }
}

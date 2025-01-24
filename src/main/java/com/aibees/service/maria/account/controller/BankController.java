package com.aibees.service.maria.account.controller;

import com.aibees.service.maria.account.domain.dto.account.ImportFileRes;
import com.aibees.service.maria.account.domain.dto.bank.*;
import com.aibees.service.maria.account.facade.AccountFacade;
import com.aibees.service.maria.account.service.bank.BankAggregate;
import com.aibees.service.maria.account.service.bank.BankImportService;
import com.aibees.service.maria.account.service.bank.BankInfoService;
import com.aibees.service.maria.common.utils.StringUtils;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    /***************************************************
     ********     Bank - Account Statement      ********
     ***************************************************/

//    /**
//     * Bank Statement 리스트 조회
//     * @param param
//     * @return
//     */
//    @GetMapping("/statement/list")
//    public ResponseEntity<ResponseData> getBankStatementList(BankStatementReq param) {
//        return bankAggregate.getBankStatementList(param);
//    }
//
//    @PostMapping("")
//    public ResponseEntity<ResponseData> saveBankStatement(@RequestBody Map<String, Object> data) {
//        return bankAggregate.saveBankStatement(data);
//    }

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
        bankAggregate.saveBankInfo(param);
    }
}

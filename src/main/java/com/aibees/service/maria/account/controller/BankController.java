package com.aibees.service.maria.account.controller;

import com.aibees.service.maria.account.domain.dto.bank.BankInfoReq;
import com.aibees.service.maria.account.domain.dto.bank.BankStatementReq;
import com.aibees.service.maria.account.facade.AccountFacade;
import com.aibees.service.maria.account.service.bank.BankAggregate;
import com.aibees.service.maria.common.vo.ResponseData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/account/bank")
@Slf4j
public class BankController {

    private final BankAggregate bankServices;
    private final AccountFacade accountFacade;

    /***************************************************
     ********     Bank - Account Statement      ********
     ***************************************************/

    /**
     * Bank Statement 리스트 조회
     * @param param
     * @return
     */
    @GetMapping("/statement/list")
    public ResponseEntity<ResponseData> getBankStatementList(BankStatementReq param) {
        log.info(param.toString());
        return bankServices.getBankStatementList(param);
    }

    @PostMapping("")
    public ResponseEntity<ResponseData> saveBankStatement(@RequestBody Map<String, Object> data) {
        return bankServices.saveBankStatement(data);
    }
    /***************************************************
     ********        Bank - Account Info        ********
     ***************************************************/

    /**
     * Bank Info 리스트 조회
     * @param param
     * @return
     */
    @GetMapping("/info/list")
    public ResponseEntity<ResponseData> getBankInfoList(BankInfoReq param) {
        log.info(param.toString());
        return bankServices.getBankInfoList(param);
    }

    /**
     * 계좌정보 마스터는 단건 별 save처리
     * @param param
     * @return
     */
    @PostMapping("/info")
    public ResponseEntity<ResponseData> saveBankInfo(@RequestBody BankInfoReq param) {
        return bankServices.saveBankInfo(param);
    }

    @PostMapping("/file")
    public ResponseEntity<ResponseData> getExcelFiles(@RequestParam("type") String type, @RequestParam("file") MultipartFile file) {
        return accountFacade.excelParse(type, file);
    }

}

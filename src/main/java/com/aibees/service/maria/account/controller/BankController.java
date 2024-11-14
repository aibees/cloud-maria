package com.aibees.service.maria.account.controller;

import com.aibees.service.maria.account.domain.dto.bank.BankInfoReq;
import com.aibees.service.maria.account.domain.dto.bank.BankStatementReq;
import com.aibees.service.maria.account.facade.AccountFacade;
import com.aibees.service.maria.account.service.bank.BankAggregate;
import com.aibees.service.maria.common.utils.StringUtils;
import com.aibees.service.maria.common.domain.entity.ResponseData;
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

    private final BankAggregate bankAggregate;
    private final AccountFacade accountFacade;

    /***************************************************
     ********       Bank - tmp Statement        ********
     ***************************************************/

    /**
     * Bank Temp Statement 리스트 조회
     * @param hashId
     * @return
     */
    @GetMapping("/temp/list")
    public ResponseEntity<ResponseData> getBankStatementTmpList(@RequestParam(name = "hashId") String hashId) {
        return bankAggregate.getBankStatementTmpList(hashId);
    }

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
        return bankAggregate.getBankStatementList(param);
    }

    @PostMapping("")
    public ResponseEntity<ResponseData> saveBankStatement(@RequestBody Map<String, Object> data) {
        return bankAggregate.saveBankStatement(data);
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
        return bankAggregate.getBankInfoList(param);
    }

    /**
     * 계좌정보 마스터는 단건 별 save처리
     * @param param
     * @return
     */
    @PostMapping("/info")
    public ResponseEntity<ResponseData> saveBankInfo(@RequestBody BankInfoReq param) {
        return bankAggregate.saveBankInfo(param);
    }

    @PostMapping("/file")
    public ResponseEntity<ResponseData> getExcelFiles(@RequestParam("type") String type, @RequestParam("file") MultipartFile file) {
        return accountFacade.excelParse(type, file);
    }

    /***************************************************
     ********        Bank - Account Info        ********
     ***************************************************/
    @GetMapping("/option")
    public ResponseEntity<ResponseData> getOptionData(@RequestParam(name = "tag")String tag) {
        if(StringUtils.isEquals(tag, "BANK_SELECT")) {
            return bankAggregate.getBankSelectList();
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

package com.aibees.service.maria.accountbook.controller;

import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.accountbook.service.AccountService;
import com.aibees.service.maria.accountbook.util.AccConstant;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/file/list")
    public Map<String, Object> getExcelImportedList(@RequestParam String fileId, @RequestParam String type) {
        return accountService.getImportExcelDataList(type, fileId);
    }

    @PostMapping("/info/transfer")
    public Map<String, Object> saveInfoDataList(@RequestBody Map<String, Object> param) {
        return accountService.transferInfoData(param);
    }

    @PostMapping("/info/list")
    public Map<String, Object> getInfoDataList(@RequestBody Map<String, Object> param) {
        return accountService.getInfoDataList(param);
    }

    @PostMapping("/list/card")
    public List<CardStatement> getCardStatementList(@RequestBody Map<String, Object> param) {
        return accountService.getCardStatementList(param);
    }

    @PostMapping("/card/paytext")
    public Map<String, Object> registCardPaymentText(@RequestBody Map<String, Object> param) {
        return accountService.registCardPaymentText(param);
    }

    @GetMapping("/card/recent")
    public List<Map<String, Object>> getRecentUseCardStatementList() {
        return accountService.getRecentUseCardStatementList();
    }

    @GetMapping("/card/status")
    public List<Map<String, Object>> getCardLimitStatusList() {
        return accountService.getRemainAmountByCard();
    }


    // common
    @PostMapping("/file")
    public Map<String, Object> getExcelFiles(
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file
    ) {

        return accountService.excelParse(type, file);
    }

    @GetMapping("/import/list")
    public List<Map<String, Object>> getFileHashList(@RequestParam String type) {
        return accountService.getFileHashList(type);
    }

    @PostMapping("/transfer")
    public Map<String, Object> transferData(@RequestBody Map<String, Object> data) {
        return accountService.transferData(data);
    }

    @GetMapping("/option")
    public List<Map<String, Object>> getAccountOption(@RequestParam String type, @RequestParam String opt) {
        if(AccConstant.IMPORT_CARD.equals(type)) {
            // card
            if("cardInfo".equals(opt)) {
                return accountService.getCardInfoForOption();
            } else {
                return null;
            }
        } else {
            // bank
            return null;
        }
    }
}

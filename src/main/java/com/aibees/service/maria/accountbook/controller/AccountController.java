package com.aibees.service.maria.accountbook.controller;

import com.aibees.service.maria.accountbook.entity.dto.CardDto;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.accountbook.service.AccountService;
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

    @PostMapping("/file")
    public Map<String, Object> getExcelFiles(
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file
            ) {

        return accountService.excelParse(type, file);
    }

    @GetMapping("/file/list")
    public List<CardStatement> getExcelImportedList(@RequestParam String fileId) {
        return accountService.getImportExcelDataList(fileId);
    }

    @PostMapping("/list")
    public List<CardStatement> getCardStatementList(@RequestBody CardDto param) {
        return accountService.getCardStatementList(param);
    }
}

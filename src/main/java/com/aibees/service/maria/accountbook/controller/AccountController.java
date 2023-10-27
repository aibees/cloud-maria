package com.aibees.service.maria.accountbook.controller;

import com.aibees.service.maria.accountbook.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/file")
    public String getExcelFiles(
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file
            ) {

        System.out.println(file.getOriginalFilename());

        accountService.excelParse(type, file);

        return "fileTest";
    }
}

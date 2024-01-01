package com.aibees.service.maria.accountbook.controller;

import com.aibees.service.maria.accountbook.service.CloseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/close")
@AllArgsConstructor
public class ClosingController {

    private final CloseService closeService;

    @GetMapping("/list")
    public Map<String, Object> getCloseDataList(@RequestParam String type, @RequestParam String ym) {
        return closeService.closeDataList(type, ym);
    }
}

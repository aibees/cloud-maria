package com.aibees.service.maria.accountbook.controller;

import com.aibees.service.maria.accountbook.service.CloseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/detail")
    public Map<String, Object> getCloseDetailList(@RequestBody Map<String, Object> param) {
        return closeService.getDetailDataListForCheck(param);
    }
}

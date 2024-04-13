package com.aibees.service.maria.account.controller;

import com.aibees.service.maria.account.service.status.StaticsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/statics")
public class StaticsController {

    private final StaticsService staticsService;

    @GetMapping("/doughnut")
    public Map<String, Object> getUsageDoughnutDatasetByYmd(@RequestParam String ym) {
        return null; // staticsService.getUsageDoughnut("USAGE", ym);
    }
}

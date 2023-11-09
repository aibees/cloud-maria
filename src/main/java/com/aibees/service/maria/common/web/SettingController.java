package com.aibees.service.maria.common.web;

import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/master")
@AllArgsConstructor
public class SettingController {

    private final SettingService commonService;

    @GetMapping("/header")
    public List<Map<String, Object>> getSettingHeaderList(
            @RequestParam(required = false) String depart,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String desc
    ) {
        Map<String, Object> param = new HashMap<>();
        param.put("depart", Optional.ofNullable(depart).orElse(""));
        param.put("category", Optional.ofNullable(category).orElse(""));
        param.put("code", Optional.ofNullable(code).orElse(""));
        param.put("desc", Optional.ofNullable(desc).orElse(""));

        return commonService.getSettingHeader(param);
    }

    @GetMapping("/detail")
    public List<Map<String, Object>> getSettingDetailList(@RequestParam String headerId) {
        return commonService.getSettingDetail(headerId);
    }

    @GetMapping("/options")
    public Map<String, Object> getSettingOptionList(@RequestParam String option) {
        List<String> optionList = commonService.getSettingOptions(option);
        Map<String, Object> result = new HashMap<>();
        result.put("type", option);
        result.put("options", optionList);
        return result;
    }
}


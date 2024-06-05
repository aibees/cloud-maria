package com.aibees.service.maria.common.controller;

import com.aibees.service.maria.common.domain.dto.CounterDto;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.CommonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comm")
@AllArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @PostMapping("/counter")
    public ResponseEntity<ResponseData> getTodayCounter(@RequestBody CounterDto param) {
        return commonService.getTodayCounter(param);
    }
}

package com.aibees.service.maria.adm.controller.impl;

import com.aibees.service.maria.adm.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class AdminControllerImpl {

    private final AdminService adminService;

    public ResponseEntity<List<String>> adminTest() {
        return ResponseEntity.ok().body(adminService.getAuthList());
    }
}

package com.aibees.service.maria.common.controller;

import com.aibees.service.maria.common.domain.dto.UserReq;
import com.aibees.service.maria.common.domain.dto.UserRes;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/gallery/login")
    public UserRes galleryMgmtLogin(@RequestBody UserReq param) {
        return userService.userLogin(param, "gallery");
    }

    @PostMapping("/ledger/login")
    public UserRes ledgerLogin(@RequestBody UserReq param) {
        return userService.userLogin(param, "ledger");
    }
}

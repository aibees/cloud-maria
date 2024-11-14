package com.aibees.service.maria.common.controller;

import com.aibees.service.maria.common.domain.dto.UserDto;
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

    @PostMapping("/login")
    public ResponseEntity<ResponseData> galleryMgmtLogin(@RequestBody UserDto param) {

        System.out.println(param.toString());


        return userService.userLogin(param);
    }
}

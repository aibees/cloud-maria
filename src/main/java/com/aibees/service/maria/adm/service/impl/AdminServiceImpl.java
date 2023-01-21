package com.aibees.service.maria.adm.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl {

    public List<String> getAuthList() {
        List<String> test = new ArrayList<>();
        test.add("test");
        return test;
    }
}

package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.multipart.dao.vo.ResourceVo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommonFileService {

    public List<String> getReousrceList() {
        List<ResourceVo> resourceList = new ArrayList<>();

        return resourceList
                .stream()
                .map(res -> {
                    return res.getPath() + "/" + res.getFilename();
                })
                .collect(Collectors.toList());
    }
}

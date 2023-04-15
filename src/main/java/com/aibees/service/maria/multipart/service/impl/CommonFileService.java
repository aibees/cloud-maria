package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.multipart.domain.vo.FileVo;
import com.aibees.service.maria.multipart.domain.vo.ResourceVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
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

    public FileVo createDirectory(FileVo vo) {
        // select FileVos from DB to find absolute path;
        FileVo parent = new FileVo();

        return parent;
    }
}

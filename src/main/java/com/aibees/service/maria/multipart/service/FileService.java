package com.aibees.service.maria.multipart.service;

import com.aibees.service.maria.multipart.dao.vo.ResourceVo;

public interface FileService {

    ResourceVo getResource(String name);

    String getUUID(String type);
}

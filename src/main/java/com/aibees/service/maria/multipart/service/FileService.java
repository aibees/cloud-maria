package com.aibees.service.maria.multipart.service;

import com.aibees.service.maria.multipart.domain.dto.FileCondition;
import org.springframework.core.io.Resource;

public interface FileService {

    Resource getResource(FileCondition condition) throws Exception;

    String getUUID(String type);
}

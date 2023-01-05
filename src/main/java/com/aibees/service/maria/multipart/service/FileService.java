package com.aibees.service.maria.multipart.service;

import com.aibees.service.maria.multipart.dao.obj.FileCondition;
import com.aibees.service.maria.multipart.dao.vo.ResourceVo;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;

public interface FileService {

    Resource getResource(@NotNull FileCondition condition) throws Exception;

    String getUUID(String type);
}

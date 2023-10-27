package com.aibees.service.maria.multipart.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileCondition {
    private long fileId;
    private long parent_id;
    private String filename;
    private String ext;
    private String createDate;
    private String updateDate;
}

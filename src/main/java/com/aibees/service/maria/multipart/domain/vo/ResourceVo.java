package com.aibees.service.maria.multipart.domain.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.File;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
public class ResourceVo {
    private String filename;
    private String fileid;
    private LocalDateTime time;
    private String path;
    private String desc;
    private File file;
}

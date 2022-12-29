package com.aibees.service.maria.multipart.dao.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
public class ImageFileVo {
    private String category;
    private String ym;
    private Long number;
    private String filename;
    private LocalDateTime createAt;
}

package com.aibees.service.maria.multipart.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileImageReq {
    private Long id;
    private String imageId;
    private String ym;
    private String ext;
    private LocalDateTime createAt;
    private LocalDateTime shotTime;
    private String displayYn;
    private long searchSize;
}

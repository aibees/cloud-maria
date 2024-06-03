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
public class FileImageDisplayRes {
    private long id;
    private String ym; // exif(shotTime)
    private String imageId;
    private Long width; // jpeg
    private Long height; // jpeg
    private LocalDateTime shotTime; // exif
    private String fullPath; // common
    private String displayYn; // common
}

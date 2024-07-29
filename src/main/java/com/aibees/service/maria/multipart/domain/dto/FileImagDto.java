package com.aibees.service.maria.multipart.domain.dto;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class FileImagDto {
    private long id;
    private String ym; // exif(shotTime)
    private String ymd;
    private String imageId;
    private String filename; // common
    private Long width; // jpeg
    private Long height; // jpeg
    private String displayYn; // common
    private String makeModel; // exit
    private String ext; // common
    private String software; // exif
    private LocalDateTime createTime; // common
    private LocalDateTime updateTime; // common
    private String fullPath; // common
    private String exposureTime;
    private String fNumber;
    private String isoRating;
    private String focalLen;
    private String lensModel;
    private String infoLock;
    private String category;
}

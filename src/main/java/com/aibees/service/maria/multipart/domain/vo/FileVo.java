package com.aibees.service.maria.multipart.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileVo {
    private long id;
    private long parentFileId;
    private String filename;
    private String ext;
    private LocalDate createAt;
}

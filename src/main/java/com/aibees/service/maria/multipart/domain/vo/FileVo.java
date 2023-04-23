package com.aibees.service.maria.multipart.domain.vo;

import com.aibees.service.maria.multipart.domain.entity.CommonFileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileVo {
    private long id;
    private long parentId;
    private String filename;
    private String ext;
    private String absoluePath;
    private LocalDateTime createAt;

    public static FileVo convertEntity(CommonFileEntity entity) {
        return FileVo.builder()
                .id(entity.getId())
                .parentId(entity.getParentId())
                .filename(entity.getFilename())
                .ext(entity.getExt())
                .createAt(entity.getCreateAt())
                .build();
    }
}

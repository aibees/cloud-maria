package com.aibees.service.maria.multipart.domain.entity;

import com.aibees.service.maria.multipart.domain.vo.FileVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="file_store")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonFileEntity {
    @Id
    private long id;
    private long parentId;
    private String filename;
    private String ext;
    private LocalDateTime createAt;

    public static CommonFileEntity convert(FileVo vo) {
        return CommonFileEntity.builder()
                .id(vo.getId())
                .parentId(vo.getParentId())
                .filename(vo.getFilename())
                .ext(vo.getExt())
                .createAt(vo.getCreateAt())
                .build();
    }
}

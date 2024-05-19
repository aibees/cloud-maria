package com.aibees.service.maria.multipart.domain.entity;

import com.aibees.service.maria.multipart.domain.vo.FileVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="file_store")
public class CommonFileEntity {
    @Id
    private long id;
    private String filename;
    private long parentId;
    @Column(name="parent_name")
    private String parentName;
    private String ext;
    private LocalDateTime createAt;
    private String alias;
}

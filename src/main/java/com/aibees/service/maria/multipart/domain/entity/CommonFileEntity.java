package com.aibees.service.maria.multipart.domain.entity;

import com.aibees.service.maria.multipart.domain.vo.FileVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private String alias;
}

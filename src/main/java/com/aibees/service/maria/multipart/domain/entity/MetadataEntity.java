package com.aibees.service.maria.multipart.domain.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="file_metadata")
@ToString
public class MetadataEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="fileid")
    private String fileid;

    @Column(name="filename")
    private String filename;

    @Column(name="path")
    private String path;

    @Column(name="desc")
    private String desc;

    @Column(name="createtime")
    private LocalDateTime createAt;

}

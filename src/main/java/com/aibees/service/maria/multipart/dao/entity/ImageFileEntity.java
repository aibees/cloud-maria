package com.aibees.service.maria.multipart.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@ToString
@Table(name="file_image")
public class ImageFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String category;
    private String ym;
    private long number;
    private String filename;

    @Column(name="create_at")
    private LocalDateTime createdAt;
}

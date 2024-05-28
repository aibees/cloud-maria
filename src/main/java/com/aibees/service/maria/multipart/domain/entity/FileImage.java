package com.aibees.service.maria.multipart.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="file_image")
public class FileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String ym; // exif(shotTime)
    @Column(name="image_id")
    private String imageId;
    private String filename; // common
    private Long width; // jpeg
    private Long height; // jpeg
    @Column(name="make_model")
    private String makeModel; // exit
    private String ext; // common
    private String software; // exif
    @Column(name="shot_time")
    private LocalDateTime shotTime; // exif
    @Column(name="create_time")
    private LocalDateTime createTime; // common
    @Column(name="full_path")
    private String fullPath; // common
    @Column(name="display_yn")
    private String displayYn; // common
}

package com.aibees.service.maria.multipart.domain.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
public class ImageFileVo {
    private Resource image;         /* image resource */
    private Long id;                /* DB seq Id */
    private String category;        /* 이미지 카테고리 */
    private String ym;              /* 이미지 년월(카테고리 하위) */
    private Long number;            /* 동년월 내 순번 */
    private String filename;        /* 파일명 */
    private LocalDateTime createAt; /* 저장일자 */
}

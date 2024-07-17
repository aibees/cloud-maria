package com.aibees.service.maria.multipart.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageableDto {
    private long searchSize;
    private long searchPage;
}

package com.aibees.service.maria.common.domain.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseData {
    private Object data;
    private String message;
}

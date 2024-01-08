package com.aibees.service.maria.common.vo;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseData {
    private HttpStatus status;
    private Object data;
    private String message;
}

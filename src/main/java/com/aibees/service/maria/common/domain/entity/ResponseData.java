package com.aibees.service.maria.common.domain.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseData<T> {
    private boolean success;
    private T data;
    private ErrorEntity error;
}

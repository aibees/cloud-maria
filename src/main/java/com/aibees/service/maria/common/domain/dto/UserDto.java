package com.aibees.service.maria.common.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class UserDto {
    private String email;
    private String password;
}

package com.aibees.service.maria.common.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserRes {
    private String accessToken;
    private String name;
    private String admin;
    private List<UserRole> roleList;
    private LocalDateTime accessTime;
}

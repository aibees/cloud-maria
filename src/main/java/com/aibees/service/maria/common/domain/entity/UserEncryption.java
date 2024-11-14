package com.aibees.service.maria.common.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Builder
@ToString
@Table(name="user_encryption")
public class UserEncryption {
    @Id
    private Long uuid;
    private String accessToken;
    private LocalDateTime accessExpiredTime;
    private String refreshToken;
    private LocalDateTime refreshExpiredTime;
}

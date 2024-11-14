package com.aibees.service.maria.common.domain.entity;

import lombok.*;

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
@Table(name="user_detail")
public class UserDetail {
    @Id
    private Long uuid;
    private String password;
    private String errcnt;
    private String otp;
}

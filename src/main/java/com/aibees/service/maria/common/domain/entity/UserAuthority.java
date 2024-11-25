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
@Table(name = "user_authority")
public class UserAuthority {
    @Id
    private String systemCd;
    private String loginKey;
}

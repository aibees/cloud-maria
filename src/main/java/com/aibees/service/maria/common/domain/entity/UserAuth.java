package com.aibees.service.maria.common.domain.entity;

import com.aibees.service.maria.common.domain.entity.pk.UserAuthPk;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Builder
@ToString
@Table(name = "user_auth")
@IdClass(UserAuthPk.class)
public class UserAuth {
    @Id
    private Long uuid;
    @Id
    private String authCd;
    private String authNm;
    private String enabledFlag;
}

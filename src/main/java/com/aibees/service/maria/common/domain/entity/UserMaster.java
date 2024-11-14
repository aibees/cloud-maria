package com.aibees.service.maria.common.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Builder
@ToString
@Table(name = "user_master")
public class UserMaster {
    @Id
    private Long uuid;
    private String email;
    private String emailPostfix;
    private String name;
    private String phone;
    private LocalDateTime createDate;
    private String admin;
}

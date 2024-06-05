package com.aibees.service.maria.common.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Builder
@Table(name="common_history")
public class CommonHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uri;
    @Column(name="user_agent")
    private String userAgent;
    @Column(name="create_time")
    private LocalDateTime createTime;
}

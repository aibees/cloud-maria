package com.aibees.service.maria.common.domain.entity;

import com.aibees.service.maria.common.domain.entity.pk.CommonCounterPk;
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
@Table(name="common_counter")
@IdClass(CommonCounterPk.class)
public class CommonCounter {
    @Id
    private String division;
    @Id
    private String cntDate;
    private Long cnt;
}

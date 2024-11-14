package com.aibees.service.maria.account.domain.entity.system;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "ma_source")
public class Source {
    @Id
    private String sourceCd;
    private String sourceNm;
    private String enabledFlag;
    private String attribute1;
    private String attribute2;
    private String attribute3;
}

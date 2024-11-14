package com.aibees.service.maria.account.domain.entity.system;

import com.aibees.service.maria.account.domain.entity.system.pk.CategoryPk;
import com.aibees.service.maria.account.domain.entity.system.pk.PresetDetailPk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "ma_category")
@IdClass(CategoryPk.class)
public class Category {
    @Id
    private String sourceCd;
    @Id
    private String categoryCd;
    private String categoryNm;
    private String enabledFlag;
}

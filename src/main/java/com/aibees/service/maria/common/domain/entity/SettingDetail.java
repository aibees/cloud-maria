package com.aibees.service.maria.common.domain.entity;

import com.aibees.service.maria.common.domain.entity.pk.SettingDetailPk;
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
@Table(name="account_setting_detail")
@IdClass(SettingDetailPk.class)
public class SettingDetail {
    @Id
    private Long headerId;
    @Id
    private String code;
    private String name;
    private int sort;
    private String enabledFlag;
    private String attribute01;
    private String attribute02;
    private String attribute03;
    private String attribute04;
    private String attribute05;

}

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
@Table(name="account_setting_header")
public class SettingHeader {
    @Id
    private Long id;
    private String mainCategory;
    private String subCategory;
    private String code;
    private String desc;
    private String attributeNm01;
    private String attributeNm02;
    private String attributeNm03;
    private String attributeNm04;
    private String attributeNm05;
}

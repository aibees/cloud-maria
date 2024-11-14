package com.aibees.service.maria.account.domain.entity.account;

import javax.persistence.*;

import com.aibees.service.maria.account.domain.entity.account.pk.AccountSettingId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="account_setting_header")
@IdClass(AccountSettingId.class)
public class AccountSetting {
    @Id
    @Column(name="id")
    private long headerId;
    @Id
    private String mainCategory;
    @Id
    private String subCategory;
    @Id
    private String code;
    private String desc;
    private String attributeNm01;
    private String attributeNm02;
    private String attributeNm03;
    private String attributeNm04;
    private String attributeNm05;
}

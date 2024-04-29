package com.aibees.service.maria.account.domain.entity.account;


import com.aibees.service.maria.account.domain.entity.account.pk.AccountSettingDetailId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="account_setting_detail")
@IdClass(AccountSettingDetailId.class)
public class AccountSettingDetail {
    @Id
    private long headerId;
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

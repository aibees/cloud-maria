package com.aibees.service.maria.accountbook.entity.vo;

import com.aibees.service.maria.accountbook.entity.vo.pk.BankStatementId;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="account_bank_statement")
@IdClass(BankStatementId.class)
public class BankStatement {
    private String fileHash;
    @Id
    private String ymd;
    @Id
    private String times;
    @Id
    private String bankId;
    @Id
    @Column(name="entry")
    private String entryCd;
    private String bankNm;
    private String bankAcct;
    private String usageCd;
    private String usageNm;
    private String entryNm;
    private String usageColor;
    private Long amount;
    private String remark;
    private String confirmStatus;
    private String wasteCheck;
}

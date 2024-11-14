package com.aibees.service.maria.account.domain.entity.account;

import com.aibees.service.maria.account.domain.entity.account.pk.JournalLinesPk;
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
@Builder
@Entity
@Table(name = "account_journal_lines")
@IdClass(JournalLinesPk.class)
public class JournalLines {
    @Id
    private long jeHeaderId;
    @Id
    private long lineNo;
    private String acctCd;
    private long amountDr;
    private long amountCr;
    private String remark;
    private String attribute1;
    private String attribute2;
}

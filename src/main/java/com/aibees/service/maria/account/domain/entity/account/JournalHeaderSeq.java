package com.aibees.service.maria.account.domain.entity.account;

import com.aibees.service.maria.account.domain.entity.account.pk.JournalHeaderSeqPk;
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
@Table(name = "account_journal_header_id")
@IdClass(JournalHeaderSeqPk.class)
public class JournalHeaderSeq {
    @Id
    private String ym;
    @Id
    private String sourceCd;
    @Id
    private long seq;
}

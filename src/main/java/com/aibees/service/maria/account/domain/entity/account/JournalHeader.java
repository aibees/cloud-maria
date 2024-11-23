package com.aibees.service.maria.account.domain.entity.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "account_journal_header")
public class JournalHeader {
    @Id
    private long jeHeaderId;
    private String jeHeaderNo;
    private LocalDate jeDate;
    private String bankId;
    private String sourceCd;
    private String categoryCd;
    private String remark;
    private String status;
    private String internalYn;
    private LocalDateTime createDate;
}

package com.aibees.service.maria.account.domain.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JournalHeaderRes {
    private long jeHeaderId;
    private String jeHeaderNo;
    private String jeDate;
    private String bankId;
    private String sourceCd;
    private String categoryCd;
    private String remark;
    private String status;
    private String trxType;
    private LocalDateTime createDate;
    List<JournalLinesRes> jeLineList;
}

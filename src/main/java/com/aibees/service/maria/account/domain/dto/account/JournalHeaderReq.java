package com.aibees.service.maria.account.domain.dto.account;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class JournalHeaderReq {
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
    private String internalYn;
    List<JournalLinesReq> jeLineList;
}

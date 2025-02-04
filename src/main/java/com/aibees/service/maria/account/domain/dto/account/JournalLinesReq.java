package com.aibees.service.maria.account.domain.dto.account;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class JournalLinesReq {
    private long jeHeaderId;
    private long lineNo;
    private String acctCd;
    private long amountDr;
    private long amountCr;
    private String remark;
    private String attribute1;
    private String attribute2;

    // for Query
    private String ym;
    private String flowType;
    private List<String> acctCdList;
}

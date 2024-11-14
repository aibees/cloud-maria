package com.aibees.service.maria.account.domain.dto.account;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class JournalHeaderSeqReq {
    private String ym;
    private String sourceCd;
    private long seq;
}

package com.aibees.service.maria.accountbook.entity.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BankDto {
    private String cardNo;
    private String ymdFrom;
    private String ymdTo;
    private String remark;
    private String usage;
    private Long amountFrom;
    private Long amountTo;
}

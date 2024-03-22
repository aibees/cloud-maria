package com.aibees.service.maria.accountbook.entity.dto;

import com.aibees.service.maria.accountbook.util.AccConstant;
import com.google.common.collect.ImmutableMap;
import lombok.*;
import org.apache.commons.codec.binary.StringUtils;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CardDto {
    private String cardNo;
    private String ymdFrom;
    private String ymdTo;
    private String remark;
    private String usageCd;
    private Long amountFrom;
    private Long amountTo;

    public Map<String, Object> toMap() {
        return new HashMap<>() {
            {
                put("cardNo", StringUtils.equals(getCardNo(), "-1") ? AccConstant.EMPTY_STR : getCardNo());
                put("ymdFrom", getYmdFrom());
                put("ymdTo", getYmdTo());
                put("remark", getRemark());
                put("usageCd", StringUtils.equals(getUsageCd(), "-1") ? AccConstant.EMPTY_STR : getUsageCd());
                put("amountFrom", getAmountFrom());
                put("amountTo", getAmountTo());
            }
        };
    }
}

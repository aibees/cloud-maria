package com.aibees.service.maria.accountbook.entity.staticsVo;

import lombok.*;


@Getter
@Setter
public class UsageData extends Data {
    public UsageData() {
        super(0);
    }

    public UsageData(String usageCd, String usageNm, String usageColor, Integer sumAmt) {
        super(sumAmt);
        this.usageCd = usageCd;
        this.usageNm = usageNm;
        this.usageColor = usageColor;
    }

    private String usageCd;
    private String usageNm;
    private String usageColor;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "( usageCd=" + usageCd + ", usageNm=" + usageNm + ", usageColor=" + usageColor + ", amount=" + super.getAmount() + " )";
    }
}
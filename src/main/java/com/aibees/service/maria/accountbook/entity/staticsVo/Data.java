package com.aibees.service.maria.accountbook.entity.staticsVo;

public abstract class Data {
    public Data(int amount) {
        this.amount = amount;
    }

    private int amount; // 수치

    public void sum(int amt) {
        amount += amt;
    }

    public int getAmount() {
        return this.amount;
    }
}

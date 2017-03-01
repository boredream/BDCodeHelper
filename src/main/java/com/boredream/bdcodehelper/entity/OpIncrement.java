package com.boredream.bdcodehelper.entity;

public class OpIncrement {

    private String __op;
    private Object amount;

    public String get__op() {
        return __op;
    }

    public Object getAmount() {
        return amount;
    }

    public OpIncrement(Object amount) {
        this.__op = "Increment";
        this.amount = amount;
    }
}

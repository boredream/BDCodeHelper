package com.boredream.bdcodehelper.entity;

/**
 * 云函数返回数据
 */
public class CloudResult<T> {

    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}

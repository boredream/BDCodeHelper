package com.boredream.bdcodehelper.entity;

/**
 * 云函数返回数据
 */
public class CloudResponse<T> {

    // TODO: 2017/6/30 error

    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}

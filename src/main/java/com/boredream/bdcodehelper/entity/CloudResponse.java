package com.boredream.bdcodehelper.entity;

public class CloudResponse<T> extends BaseResponse {

    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}

package com.boredream.bdcodehelper.lean.entity;

public class BaseResponse {

    private int code;
    private String error;

    public boolean success() {
        return code == 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}

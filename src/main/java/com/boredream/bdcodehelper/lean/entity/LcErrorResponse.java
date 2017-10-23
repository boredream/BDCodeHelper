package com.boredream.bdcodehelper.lean.entity;

public class LcErrorResponse extends Throwable {

    public LcErrorResponse(BaseResponse error) {
        super();
        this.error = error;
    }

    private BaseResponse error;

    public BaseResponse getError() {
        return error;
    }

    public void setError(BaseResponse error) {
        this.error = error;
    }
}

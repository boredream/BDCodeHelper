package com.boredream.bdcodehelper.net;

import com.boredream.bdcodehelper.entity.BaseResponse;

public class LeanCloudError extends Throwable {

    public LeanCloudError(BaseResponse error) {
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

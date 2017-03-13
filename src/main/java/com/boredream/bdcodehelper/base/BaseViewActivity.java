package com.boredream.bdcodehelper.base;

import android.os.Bundle;

public class BaseViewActivity extends BoreBaseActivity implements BaseView{

    private boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isActive = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        isActive = false;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void showTip(String msg) {
        showToast(msg);
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void dismissProgress() {
        dismissProgressDialog();
    }
}

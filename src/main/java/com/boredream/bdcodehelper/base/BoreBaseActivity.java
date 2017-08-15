package com.boredream.bdcodehelper.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.boredream.bdcodehelper.utils.DialogUtils;
import com.boredream.bdcodehelper.utils.ToastUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class BoreBaseActivity extends RxAppCompatActivity implements BaseView {

    protected String TAG;
    // TODO: view替代dialog作为progress，同时支持empty和error、retry等操作
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setScreenOrientation();
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName();
        progressDialog = DialogUtils.createProgressDialog(this);
    }

    @Override
    public <T> LifecycleTransformer<T> getLifeCycleTransformer() {
        return bindUntilEvent(ActivityEvent.DESTROY);
    }

    @Override
    public void showTip(String msg) {
        ToastUtils.showToast(this, msg);
    }

    @Override
    public void showProgress() {
        if(progressDialog.isShowing()) {
            return;
        }
        progressDialog.show();
    }

    @Override
    public void dismissProgress() {
        if(!progressDialog.isShowing()) {
            return;
        }
        progressDialog.dismiss();
    }

    protected void setScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 跳转页面,无extra简易型
     *
     * @param tarActivity 目标页面
     */
    public void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }

    public void showLog(String msg) {
        Log.i(TAG, msg);
    }
}

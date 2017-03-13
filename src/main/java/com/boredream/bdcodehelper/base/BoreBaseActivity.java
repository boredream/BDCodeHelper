package com.boredream.bdcodehelper.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.utils.DialogUtils;
import com.boredream.bdcodehelper.utils.ToastUtils;

public class BoreBaseActivity extends AppCompatActivity implements BaseView{

    protected String TAG;
    private Dialog progressDialog;
    private boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setScreenOrientation();
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName();
        progressDialog = DialogUtils.createProgressDialog(this);
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

    public void showToast(String msg) {
        ToastUtils.showToast(this, msg, Toast.LENGTH_SHORT);
    }

    public void showLog(String msg) {
        Log.i(TAG, msg);
    }

    public void showProgressDialog() {
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

}

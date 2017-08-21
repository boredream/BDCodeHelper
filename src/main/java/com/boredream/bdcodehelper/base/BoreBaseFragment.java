package com.boredream.bdcodehelper.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.boredream.bdcodehelper.utils.DialogUtils;
import com.boredream.bdcodehelper.utils.ToastUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

public class BoreBaseFragment extends RxFragment implements BaseView {

    // TODO: view替代dialog作为progress，同时支持empty和error、retry等操作
    private Dialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = DialogUtils.createProgressDialog(getActivity());
    }

    @Override
    public <T> LifecycleTransformer<T> getLifeCycleTransformer() {
        return bindUntilEvent(FragmentEvent.DESTROY);
    }

    @Override
    public void showTip(String msg) {
        ToastUtils.showToast(getActivity(), msg);
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
}

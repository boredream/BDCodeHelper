package com.boredream.bdcodehelper.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class DialogUtils {

    public static Dialog createProgressDialog(Context context) {
        return createProgressDialog(context, false);
    }

    public static Dialog createProgressDialog(Context context, boolean needCancle) {
        Dialog dialog = new ProgressDialog(context);
        dialog.setCancelable(needCancle);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog showCommonDialog(Context context, String message,
                                         DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .show();
    }

    public static Dialog showConfirmDialog(Context context, String message,
                                         DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("确定", listener)
                .show();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////


}

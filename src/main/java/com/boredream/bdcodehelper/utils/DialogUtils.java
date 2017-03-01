package com.boredream.bdcodehelper.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.boredream.bdcodehelper.R;

/**
 * 对话框工具类, 提供常用对话框显示, 使用support.v7包内的AlertDialog样式
 */
public class DialogUtils {

    public static Dialog createProgressDialog(Context context) {
        return createProgressDialog(context, true);
    }

    public static Dialog createProgressDialog(Context context, boolean needCancle) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(needCancle);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog showCommonDialog(Context context, String message,
                                        String posBtn, String rightBtn,
                                        DialogInterface.OnClickListener posListener) {
        return showCommonDialog(context, message, posBtn, rightBtn, posListener, null);
    }

    public static Dialog showCommonDialog(Context context, String message,
                                          String posBtn, String rightBtn,
                                          DialogInterface.OnClickListener posListener,
                                          DialogInterface.OnClickListener negListener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(posBtn, posListener)
                .setNegativeButton(rightBtn, negListener)
                .show();
    }


    public static Dialog showCommonDialog(Context context, String message,
                                          DialogInterface.OnClickListener listener) {
        return showCommonDialog(context,
                message,
                context.getString(R.string.dialog_positive),
                context.getString(R.string.dialog_negative),
                listener);
    }

    public static Dialog showConfirmDialog(Context context, String message,
                                           DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.dialog_positive), listener)
                .show();
    }

}

package com.boredream.bdcodehelper.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.R;

public class SettingItemView extends LinearLayout {

    private ImageView iv_left;
    private TextView tv_mid;
    private TextView tv_right;
    private ImageView iv_right;

    public SettingItemView(Context context) {
        super(context);
        init(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    // TODO: 2017/7/18 diver line

    // TODO: 2017/7/18 attr

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_setting, this);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_mid = (TextView) findViewById(R.id.tv_mid);
        tv_right = (TextView) findViewById(R.id.tv_right);
        iv_right = (ImageView) findViewById(R.id.iv_right);
    }

    public SettingItemView setLeftImg(int imgRes) {
        if(imgRes < 0) {
            iv_left.setVisibility(View.GONE);
        } else {
            iv_left.setVisibility(View.VISIBLE);
            iv_left.setImageResource(imgRes);
        }
        return this;
    }

    public SettingItemView setMidText(@NonNull String text) {
        tv_mid.setText(text);
        return this;
    }

    public SettingItemView setRightText(String text) {
        if (TextUtils.isEmpty(text)) {
            tv_right.setVisibility(View.GONE);
        } else {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(text);
        }
        return this;
    }

    public SettingItemView setRightImg(int imgRes) {
        if(imgRes < 0) {
            iv_right.setVisibility(View.GONE);
        } else {
            iv_right.setVisibility(View.VISIBLE);
            iv_right.setImageResource(imgRes);
        }
        return this;
    }

}

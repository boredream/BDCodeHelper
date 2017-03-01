package com.boredream.bdcodehelper.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.utils.DisplayUtils;

public class TitlebarView extends RelativeLayout {

    private Resources resources;

    private int paddingHorizontal;
    private int paddingVertical;

    private TextView tvTitle;
    private ImageView ivLeft;
    private ImageView ivRight;
    private TextView tvLeft;
    private TextView tvRight;

    public TitlebarView(Context context) {
        super(context);
        init();
    }

    public TitlebarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitlebarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TitlebarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        resources = getContext().getResources();
        setBackgroundColor(resources.getColor(R.color.colorPrimary));

        paddingHorizontal = DisplayUtils.dp2px(getContext(), 12);
        paddingVertical = DisplayUtils.dp2px(getContext(), 16);

        // 标题是标配~~
        tvTitle = createTitleText();
    }

    /**
     * 设置标题文字
     *
     * @param text 文字内容
     */
    public void setTitleText(CharSequence text) {
//        tvTitle.setText(text);
    }

    /**
     * 设置左边文字
     *
     * @param text 文字内容
     */
    public void setLeftText(CharSequence text) {
        if (tvLeft != null) {
            // 如果创建过，则重新显示即可
            tvLeft.setVisibility(View.VISIBLE);
        } else {
            // 新建
            tvLeft = createBtnText();
        }

        // 显示文字，则隐藏图片
        if (ivLeft != null) {
            ivLeft.setVisibility(View.GONE);
        }

        tvLeft.setText(text);
    }

    /**
     * 设置左边图片
     *
     * @param resId 图片资源id
     */
    public void setLeftImage(int resId) {
        if (ivLeft != null) {
            // 如果创建过，则重新显示即可
            ivLeft.setVisibility(View.VISIBLE);
        } else {
            // 新建
            ivLeft = createBtnImage();
        }

        // 显示图片，则隐藏文字
        if (tvLeft != null) {
            tvLeft.setVisibility(View.GONE);
        }

        ivLeft.setImageResource(resId);
    }

    /**
     * 设置右边边文字
     *
     * @param text 文字内容
     */
    public void setRightText(CharSequence text) {
        if (tvRight != null) {
            // 如果创建过，则重新显示即可
            tvRight.setVisibility(View.VISIBLE);
        } else {
            // 新建
            tvRight = createBtnText();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvRight.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }

        // 显示文字，则隐藏图片
        if (ivRight != null) {
            ivRight.setVisibility(View.GONE);
        }

        tvRight.setText(text);
    }

    /**
     * 设置右边图片
     *
     * @param resId 图片资源id
     */
    public void setRightImage(int resId) {
        if (ivRight != null) {
            // 如果创建过，则重新显示即可
            ivRight.setVisibility(View.VISIBLE);
        } else {
            // 新建
            ivRight = createBtnImage();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRight.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }

        // 显示图片，则隐藏文字
        if (tvRight != null) {
            tvRight.setVisibility(View.GONE);
        }

        ivRight.setImageResource(resId);
    }

    /**
     * 设置左边点击事件
     *
     * @param listener 点击事件
     */
    public void setLeftOnClickListener(View.OnClickListener listener) {
        if (ivLeft != null && ivLeft.getVisibility() == View.VISIBLE) {
            ivLeft.setOnClickListener(listener);
        } else if (tvLeft != null && tvLeft.getVisibility() == View.VISIBLE) {
            tvLeft.setOnClickListener(listener);
        }
    }

    /**
     * 设置右边点击事件
     *
     * @param listener 点击事件
     */
    public void setRightOnClickListener(View.OnClickListener listener) {
        if (ivRight != null && ivRight.getVisibility() == View.VISIBLE) {
            ivRight.setOnClickListener(listener);
        } else if (tvRight != null && tvRight.getVisibility() == View.VISIBLE) {
            tvRight.setOnClickListener(listener);
        }
    }

    private TextView createTitleText() {
        TextView tv = new TextView(getContext());
        tv.setTextColor(resources.getColor(R.color.white));
        tv.setTextSize(22);
        tv.setMaxWidth(DisplayUtils.dp2px(getContext(), 280));

        LayoutParams tvParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tv.setLayoutParams(tvParams);
        addView(tv);

        return tv;
    }

    private ImageView createBtnImage() {
        ImageView iv = new ImageView(getContext());
        RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(
                DisplayUtils.dp2px(getContext(), 40), LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(ivParams);
        iv.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        iv.setBackgroundResource(R.drawable.bg_tran2dark_sel);
        addView(iv);

        return iv;
    }

    private TextView createBtnText() {
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(16);
        tv.setGravity(Gravity.CENTER);

        LayoutParams tvParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(tvParams);
        tv.setPadding(paddingHorizontal, 0, paddingHorizontal, 0);
        tv.setBackgroundResource(R.drawable.bg_tran2dark_sel);
        addView(tv);

        return tv;
    }


}

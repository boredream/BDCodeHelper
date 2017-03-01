package com.boredream.bdcodehelper.view;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.utils.DisplayUtils;

public class IndicatorRadioGroup extends RadioGroup {

    private int indicatorRes;

    public void setIndicatorRes(int indicatorRes) {
        this.indicatorRes = indicatorRes;
    }

    public IndicatorRadioGroup(Context context) {
        super(context);
        init(context);
    }

    public IndicatorRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        indicatorRes = R.drawable.shape_oval_primary_stroke2solid_sel;
    }

    public void setViewPager(ViewPager vp, final int count) {
        if(vp == null || vp.getAdapter() == null) {
            return;
        }

        if (count <= 1) {
            setVisibility(View.GONE);
            return;
        }

        setVisibility(View.VISIBLE);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (getChildCount() > 1) {
                    ((RadioButton) getChildAt(position % count)).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        removeAllViews();
        for (int i = 0; i < count; i++) {
            RadioButton rb = new RadioButton(getContext());
            LayoutParams params = new LayoutParams(
                    DisplayUtils.dp2px(getContext(), 6), DisplayUtils.dp2px(getContext(), 6));
            if (i > 0) {
                params.setMargins(DisplayUtils.dp2px(getContext(), 10), 0, 0, 0);
            }
            rb.setLayoutParams(params);
            rb.setButtonDrawable(new ColorDrawable());
            rb.setBackgroundResource(indicatorRes);
            rb.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    // do nothing
                    return true;
                }
            });
            addView(rb);
        }

        ((RadioButton) getChildAt(0)).setChecked(true);
    }
}

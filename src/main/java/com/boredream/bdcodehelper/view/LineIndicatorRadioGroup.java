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

public class LineIndicatorRadioGroup extends RadioGroup {

    public LineIndicatorRadioGroup(Context context) {
        super(context);
    }

    public LineIndicatorRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewPager(ViewPager vp, final int count) {
        if (vp == null || vp.getAdapter() == null) {
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
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(0, DisplayUtils.dp2px(getContext(), 3));
            params.weight = 1;
            rb.setLayoutParams(params);
            rb.setButtonDrawable(new ColorDrawable());
            rb.setBackgroundResource(R.drawable.bg_line_indicator_sel);
            rb.setOnTouchListener(new View.OnTouchListener() {
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

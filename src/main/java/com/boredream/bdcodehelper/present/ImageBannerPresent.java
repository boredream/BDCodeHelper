package com.boredream.bdcodehelper.present;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.adapter.BannerPagerAdapter;
import com.boredream.bdcodehelper.entity.ImageUrlInterface;
import com.boredream.bdcodehelper.view.IndicatorRadioGroup;
import com.boredream.bdcodehelper.view.LineIndicatorRadioGroup;

import java.util.ArrayList;

public class ImageBannerPresent<T extends ImageUrlInterface> {

    private static final String TAG = ImageBannerPresent.class.getSimpleName();

    private static final int AUTO_SCROLL_GAP_TIME = 8000;
    private static final int STATE_STOP = 0;
    private static final int STATE_AUTO_SCROLLING = 1;
    private int currentState;

    private Context context;
    private final CountDownTimer countDownTimer;

    private ViewPager vp_banner;
    private LineIndicatorRadioGroup rg_indicator;
    private BannerPagerAdapter<T> adapter;

    public ImageBannerPresent(View include_banner_with_indicator) {
        this.context = include_banner_with_indicator.getContext();

        vp_banner = (ViewPager) include_banner_with_indicator.findViewById(R.id.vp_banner);
        rg_indicator = (LineIndicatorRadioGroup) include_banner_with_indicator.findViewById(R.id.rg_indicator);

        countDownTimer = new CountDownTimer(
                Long.MAX_VALUE, AUTO_SCROLL_GAP_TIME) {
            @Override
            public void onTick(long l) {
                vp_banner.setCurrentItem(vp_banner.getCurrentItem() + 1);
            }

            @Override
            public void onFinish() {

            }
        };
    }

    public void load(ArrayList<T> images, BannerPagerAdapter.OnBannerClickListener listener) {
        adapter = new BannerPagerAdapter<>(context, images);
        vp_banner.setAdapter(adapter);
        adapter.setOnBannerClickListener(listener);

        rg_indicator.setViewPager(vp_banner, adapter.getImages().size());

        startAutoScroll();
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        rg_indicator.setViewPager(vp_banner, adapter.getImages().size());
    }

    public void startAutoScroll() {
        stopAutoScroll();

        if (currentState == STATE_AUTO_SCROLLING) {
            return;
        }

        currentState = STATE_AUTO_SCROLLING;
        countDownTimer.start();
    }

    public void stopAutoScroll() {
        currentState = STATE_STOP;
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}

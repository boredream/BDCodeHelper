package com.boredream.bdcodehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boredream.bdcodehelper.activity.ImageBrowserActivity;
import com.boredream.bdcodehelper.entity.ImageUrlInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class BannerPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<? extends ImageUrlInterface> urls;

    public BannerPagerAdapter(Context context, ArrayList<? extends ImageUrlInterface> urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        if (urls.size() > 1) {
            return Integer.MAX_VALUE;
        }
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView iv = new ImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                container.getWidth(), container.getHeight());
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final String url = urls.get(position % urls.size()).getImageUrls();

        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.mipmap.ic_account_circle_grey600_24dp)
//                .error(R.mipmap.ic_account_circle_grey600_24dp)
                .centerCrop()
                .crossFade()
                .into(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageBrowserActivity.class);
                intent.putExtra("images", urls);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });

        container.addView(iv);
        return iv;
    }

}

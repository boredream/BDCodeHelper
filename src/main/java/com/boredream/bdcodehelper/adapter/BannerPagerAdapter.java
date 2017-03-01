package com.boredream.bdcodehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.activity.ImageBrowserActivity;
import com.boredream.bdcodehelper.activity.WebViewActivity;
import com.boredream.bdcodehelper.entity.ImageUrlInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class BannerPagerAdapter<T extends ImageUrlInterface> extends PagerAdapter {

    private Context context;
    private ArrayList<T> images;

    public ArrayList<T> getImages() {
        return images;
    }

    public BannerPagerAdapter(Context context, ArrayList<T> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        if (images.size() > 1) {
            return Integer.MAX_VALUE;
        }
        return images.size();
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
        final ImageUrlInterface image = images.get(position % images.size());

        View view = View.inflate(context, R.layout.item_image_banner, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        final ImageView iv = (ImageView) view.findViewById(R.id.iv_image);

        String title = image.getImageTitle();
        if (TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.GONE);
        } else {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        }

        final String url = image.getImageUrl();
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onBannerClickListener != null) {
                    onBannerClickListener.onOnClick(position % images.size());
                } else {
                    if (!TextUtils.isEmpty(image.getImageLink())) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", image.getImageTitle());
                        intent.putExtra("url", image.getImageLink());
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, ImageBrowserActivity.class);
                        intent.putExtra("images", images);
                        intent.putExtra("position", position % images.size());
                        context.startActivity(intent);
                    }
                }
            }
        });

        container.addView(view);
        return view;
    }

    private OnBannerClickListener onBannerClickListener;

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }

    public static interface OnBannerClickListener {
        void onOnClick(int position);
    }

}

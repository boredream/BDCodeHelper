package com.boredream.bdcodehelper.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.boredream.bdcodehelper.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

// FIXME: 2017/8/15 和 ImageBrowserAdapter合并
public class ImageStrBrowserAdapter extends PagerAdapter {

    protected Activity context;
    protected List<String> imageUrls;
    private Map<Integer, WeakReference<ImageView>> imageViews = new HashMap<>();

    public ImageStrBrowserAdapter(Activity context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    public ImageView getImageView(int position) {
        int index = position % imageUrls.size();
        WeakReference<ImageView> weakReference = imageViews.get(index);
        if(weakReference == null) return null;
        return weakReference.get();
    }

    @Override
    public int getCount() {
        if (imageUrls.size() > 1) {
            return Integer.MAX_VALUE;
        }
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(final ViewGroup container, int position) {
        final View rootView = View.inflate(context, R.layout.item_image_browser, null);

        final ProgressBar pb_loading = (ProgressBar) rootView.findViewById(R.id.pb_loading);
        final ImageView iv_image_browser = (ImageView) rootView.findViewById(R.id.iv_image_browser);
        final PhotoViewAttacher pva = new PhotoViewAttacher(iv_image_browser);

        int index = position % imageUrls.size();
        String url = imageUrls.get(index);

        Glide.with(context)
                .load(url)
                .into(new SimpleTarget<Drawable>() {

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        pb_loading.setVisibility(View.GONE);
                        iv_image_browser.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        pb_loading.setVisibility(View.VISIBLE);
                        iv_image_browser.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        pb_loading.setVisibility(View.GONE);
                        iv_image_browser.setVisibility(View.VISIBLE);

                        iv_image_browser.setImageDrawable(resource);
                        pva.update();
                    }
                });

        pva.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                context.onBackPressed();
            }
        });

        imageViews.put(position % imageUrls.size(), new WeakReference<>(iv_image_browser));
        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        imageViews.remove(position % imageUrls.size());
        container.removeView((View) object);
    }

}
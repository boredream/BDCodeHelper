package com.boredream.bdcodehelper.net;

import android.widget.ImageView;

import com.boredream.bdcodehelper.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class GlideHelper {

    public static void loadImg(ImageView iv, String url) {
        loadImg(iv, url, R.drawable.default_image);
    }

    public static void loadImg(ImageView iv, String url, int defaultImg) {
        RequestOptions options = new RequestOptions();
        if(defaultImg != -1) {
            options = options.placeholder(defaultImg).error(defaultImg);
        }

        Glide.with(iv.getContext())
                .load(url)
                .apply(options)
                .into(iv);
    }

    public static void loadOvalImg(ImageView iv, String url) {
        loadOvalImg(iv, url, R.drawable.default_oval_avatar);
    }

    public static void loadOvalImg(ImageView iv, String url, int defaultImg) {
        RequestOptions options = new RequestOptions();
        if(defaultImg != -1) {
            options = options.placeholder(defaultImg).error(defaultImg);
        }

        Glide.with(iv.getContext())
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .apply(options)
                .into(iv);
    }

}

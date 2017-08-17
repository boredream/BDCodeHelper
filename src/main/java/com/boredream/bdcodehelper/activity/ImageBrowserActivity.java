package com.boredream.bdcodehelper.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.adapter.ImageBrowserAdapter;
import com.boredream.bdcodehelper.base.BoreBaseActivity;

import java.util.ArrayList;

public class ImageBrowserActivity extends BoreBaseActivity {

    private ViewPager vp_image_brower;
    private TextView tv_image_index;

    private int position;
    private ImageBrowserAdapter adapter;
    private ArrayList images;

    public static void start(Context context, ArrayList images, int position) {
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        intent.putExtra("images", images);
        intent.putExtra("position", position % images.size());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_browser);

        initData();
        initView();
        setData();
    }

    private void initData() {
        images = (ArrayList) getIntent().getSerializableExtra("images");
        position = getIntent().getIntExtra("position", 0);
    }

    private void initView() {
        vp_image_brower = (ViewPager) findViewById(R.id.vp_image_brower);
        tv_image_index = (TextView) findViewById(R.id.tv_image_index);
    }

    private void setData() {
        adapter = new ImageBrowserAdapter(this, images);
        vp_image_brower.setAdapter(adapter);

        int initPosition = position;

        if (images.size() > 1) {
            tv_image_index.setVisibility(View.VISIBLE);
            tv_image_index.setText((position + 1) + "/" + images.size());
        } else {
            tv_image_index.setVisibility(View.GONE);
        }

        vp_image_brower.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                int index = arg0 % images.size();
                tv_image_index.setText((index + 1) + "/" + images.size());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        vp_image_brower.setCurrentItem(initPosition);
    }
}

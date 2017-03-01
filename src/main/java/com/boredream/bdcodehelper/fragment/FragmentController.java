package com.boredream.bdcodehelper.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * fragment切换控制器, 初始化时直接add全部fragment, 然后利用show和hide进行切换控制
 */
public class FragmentController {

    private int containerId;
    private FragmentManager fm;
    private ArrayList<Fragment> fragments;
    private int curPosition;

    public FragmentController(AppCompatActivity activity, int containerId, ArrayList<Fragment> fragments) {
        this.containerId = containerId;
        this.fragments = fragments;
        this.fm = activity.getSupportFragmentManager();
        initFragment();
    }

    public void initFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            ft.add(containerId, fragments.get(i), String.valueOf(i));
        }
        ft.commit();
    }

    public void showFragment(int position) {
        curPosition = position;

        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == position) {
                // 显示position对应fragment
                ft.show(fragments.get(i));
            } else {
                // hide其他的fragment
                ft.hide(fragments.get(i));
            }
        }
        ft.commit();
    }

    private void hideFragments() {
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    public int getCurPosition() {
        return curPosition;
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }

}
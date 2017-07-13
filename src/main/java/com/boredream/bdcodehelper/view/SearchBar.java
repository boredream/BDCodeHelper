package com.boredream.bdcodehelper.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.utils.AppUtils;
import com.boredream.bdcodehelper.utils.ToastUtils;


public class SearchBar extends FrameLayout {

    private EditText et_search;
    private ImageView iv_delete;

    private OnSearchListener listener;

    public EditText getEditText() {
        return et_search;
    }

    public SearchBar(Context context) {
        super(context);
        initView();
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.include_searchbar, this);

        et_search = (EditText) findViewById(R.id.searchbar_et);
        iv_delete = (ImageView) findViewById(R.id.searchbar_iv_delete);

        iv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });
        findViewById(R.id.searchbar_tv_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });
    }

    public void initSearchBar(String hint, OnSearchListener listener) {
        this.listener = listener;
        if(!TextUtils.isEmpty(hint)) {
            et_search.setHint(hint);
        }
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                }
                return false;
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                iv_delete.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void search() {
        AppUtils.hideSoftInput(et_search);
        et_search.clearFocus();

        String searchKey = et_search.getText().toString().trim();
        if (listener != null) {
            listener.onSearch(searchKey);
        }
    }

    public interface OnSearchListener {
        void onSearch(@NonNull String searchKey);
    }
}

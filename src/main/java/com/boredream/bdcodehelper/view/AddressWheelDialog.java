package com.boredream.bdcodehelper.view;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.db.AddressData;
import com.boredream.bdcodehelper.entity.city.CityModel;
import com.boredream.bdcodehelper.view.wheel.OnWheelChangedListener;
import com.boredream.bdcodehelper.view.wheel.WheelView;
import com.boredream.bdcodehelper.view.wheel.adapters.ArrayWheelAdapter;

import java.util.ArrayList;

public class AddressWheelDialog extends Dialog implements OnWheelChangedListener {

    private String mCurrentProvince;

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private Button mBtnConfirm;
    private ArrayWheelAdapter<CityModel> cityAdapter;

    public AddressWheelDialog(Context context) {
        super(context);

        initViews();
        initData();
    }

    private void initViews() {
        setContentView(R.layout.wheel_dialog_address);

        setTitle("请选择地址");

        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);

        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onAddressSelectListener != null) {
                    int curCityItem = mViewCity.getCurrentItem();
                    CityModel city = AddressData.mCitisDatasMap.get(mCurrentProvince).get(curCityItem);
                    onAddressSelectListener.onAddressSelected(city);
                }
            }
        });
    }

    private void initData() {
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(getContext(), AddressData.mProvinceDatas));
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        updateCities();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        }
    }

    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProvince = AddressData.mProvinceDatas.get(pCurrent);
        ArrayList<CityModel> cities = AddressData.mCitisDatasMap.get(mCurrentProvince);
        if (cities == null) {
            cities = new ArrayList<>();
        }
        cityAdapter = new ArrayWheelAdapter<>(getContext(), cities);
        mViewCity.setViewAdapter(cityAdapter);
        mViewCity.setCurrentItem(0);
    }

    private OnAddressSelectListener onAddressSelectListener;

    public void setOnAddressSelectListener(OnAddressSelectListener onAddressSelectListener) {
        this.onAddressSelectListener = onAddressSelectListener;
    }

    public interface OnAddressSelectListener {
        void onAddressSelected(CityModel address);
    }

}

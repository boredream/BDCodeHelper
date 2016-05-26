package com.boredream.bdcodehelper.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boredream.bdcodehelper.entity.city.CityModel;

import java.util.ArrayList;
import java.util.HashMap;

public class AddressData {
    public static ArrayList<String> mProvinceDatas = new ArrayList<>();
    public static HashMap<String, ArrayList<CityModel>> mCitisDatasMap = new HashMap<>();

    public static void init(final Context context) {
        new Thread() {
            @Override
            public void run() {
                // 初始化，只需要调用一次
                AssetsDatabaseManager.initManager(context);
                // 获取管理对象，因为数据库需要通过管理对象才能够获取
                AssetsDatabaseManager adm = AssetsDatabaseManager.getManager();
                // 通过管理对象获取数据库
                SQLiteDatabase sql = adm.getDatabase("city");

                // city
                Cursor cursorCity = sql.rawQuery("select *,* from T_City, T_Province where T_City.ProID = T_Province.ProSort", null);
                if (!cursorCity.moveToFirst()) {
                    return;
                }
                do {
                    String province = cursorCity.getString(cursorCity.getColumnIndex("ProName"));

                    CityModel city = new CityModel();
                    city.province = province;
                    city.id = cursorCity.getString(cursorCity.getColumnIndex("CitySort"));
                    city.name = cursorCity.getString(cursorCity.getColumnIndex("CityName"));

                    if(!mProvinceDatas.contains(province)) {
                        mProvinceDatas.add(province);
                    }

                    ArrayList<CityModel> cities = mCitisDatasMap.get(province);
                    if(cities == null) {
                        cities = new ArrayList<>();
                        cities.add(city);
                        mCitisDatasMap.put(province, cities);
                    } else {
                        cities.add(city);
                    }

                } while (cursorCity.moveToNext());
            }
        }.start();

    }
}

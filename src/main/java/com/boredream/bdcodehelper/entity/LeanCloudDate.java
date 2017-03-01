package com.boredream.bdcodehelper.entity;

import com.boredream.bdcodehelper.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class LeanCloudDate implements Serializable {

    private String iso;
    private String __type;

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String get__type() {
        return __type;
    }

    public void set__type(String __type) {
        this.__type = __type;
    }

    public String getDate() {
        // 2015-08-27T07:38:33
        return iso.substring(0, 10);
    }
}

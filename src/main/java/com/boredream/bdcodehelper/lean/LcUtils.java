package com.boredream.bdcodehelper.lean;

import com.boredream.bdcodehelper.lean.entity.Pointer;

public class LcUtils {

    public static final int PAGE_SIZE = 20;

    public static int page2skip(int page) {
        return page2skip(page, PAGE_SIZE);
    }

    public static int page2skip(int page, int pageSize) {
        return (page - 1) * pageSize;
    }

    public static Pointer getPointer(Pointer entity) {
        Pointer pointer = new Pointer();
        pointer.setClassName(entity.getClassName());
        pointer.setObjectId(entity.getObjectId());
        return pointer;
    }

    public static String getDate(String date) {
        // 2017-08-15T15:25:28.353Z
        return date.substring(0, 10) + date.substring(12);
    }

}

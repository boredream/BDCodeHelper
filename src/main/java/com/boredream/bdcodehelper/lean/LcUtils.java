package com.boredream.bdcodehelper.lean;

public class LcUtils {

    public static final int PAGE_SIZE = 20;

    public static int page2skip(int page) {
        return page2skip(page, PAGE_SIZE);
    }

    public static int page2skip(int page, int pageSize) {
        return (page - 1) * pageSize;
    }

}

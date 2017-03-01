package com.boredream.bdcodehelper.utils;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {

    public static <T> boolean isEmpty(Collection<T> c) {
        return c == null || c.size() == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.size() == 0;
    }
}

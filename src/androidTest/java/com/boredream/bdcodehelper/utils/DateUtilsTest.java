package com.boredream.bdcodehelper.utils;

import org.junit.Test;

public class DateUtilsTest {

    @Test
    public void testGetAge() throws Exception {
        String birth = "1989-07-21";
        int age = DateUtils.getAge(DateUtils.str2date(birth));
        System.out.println(age);
    }
}
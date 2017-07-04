package com.boredream.bdcodehelper.entity;

/**
 * 用手机号注册用户
 */
public class UserRegisterByMobilePhone {
    /**
     * 验证手机号
     */
    private String mobilePhoneNumber;

    /**
     * 手机号验证码,发送短信验证时请求使用
     */
    private String smsCode;

    private String password;

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

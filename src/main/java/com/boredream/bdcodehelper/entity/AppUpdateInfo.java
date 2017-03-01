package com.boredream.bdcodehelper.entity;

import com.boredream.bdcodehelper.base.CommonBaseEntity;

public class AppUpdateInfo extends CommonBaseEntity {

    public static AppUpdateInfo get(String appName) {
        AppUpdateInfo appUpdateInfo = new AppUpdateInfo();
        appUpdateInfo.setAppName(appName);
        return appUpdateInfo;
    }

    private int version;
    private String versionName;
    private FileInfo apkFile;
    private String updateInfo;
    private String appName;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public FileInfo getApkFile() {
        return apkFile;
    }

    public void setApkFile(FileInfo apkFile) {
        this.apkFile = apkFile;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getAppName() {
        return appName + "_" + versionName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}

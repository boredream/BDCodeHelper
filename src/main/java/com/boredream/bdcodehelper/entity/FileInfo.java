package com.boredream.bdcodehelper.entity;

import com.boredream.bdcodehelper.base.CommonBaseEntity;

public class FileInfo extends CommonBaseEntity {
    private String cdn;
    private String filename;
    private String url;

    public String getCdn() {
        return cdn;
    }

    public void setCdn(String cdn) {
        this.cdn = cdn;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

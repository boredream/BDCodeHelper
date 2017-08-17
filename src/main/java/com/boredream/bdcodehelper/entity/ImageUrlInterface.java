package com.boredream.bdcodehelper.entity;

import java.io.Serializable;

public abstract class ImageUrlInterface implements Serializable {

    public abstract String getImageUrl();

    public abstract String getImageTitle();

    public abstract String getImageLink();

    @Override
    public String toString() {
        return getImageUrl();
    }
}

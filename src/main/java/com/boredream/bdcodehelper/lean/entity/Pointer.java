package com.boredream.bdcodehelper.lean.entity;

import java.io.Serializable;

/**
 * 数据实体基类
 * <p>
 * 之所以继承Pointer对象,是因为接口用法的限制,具体可以参考Bmob/LeanCloud/Parse的相关文档<br/>
 * 这样的接口可以让对象在提交创建和获取的时候都更加方便<br/>
 * 提交时只要添加Pointer的type和className等所需字段即可,获取时基本不用做任何额外处理
 */
public class Pointer implements Serializable {
    public static final String TYPE = "Pointer";

    protected String __type;
    protected String className;
    protected String objectId;
    protected String createdAt;
    protected String updatedAt;

    public Pointer() {
        this.__type = TYPE;
        String className = getClass().getSimpleName();
        if(className.equals("User")) {
            className = "_User";
        }
        this.className = className;
    }

    public String get__type() {
        return __type;
    }

    public void set__type(String __type) {
        this.__type = __type;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pointer pointer = (Pointer) o;
        return !(objectId != null ? !objectId.equals(pointer.objectId) : pointer.objectId != null);
    }

    @Override
    public int hashCode() {
        return objectId != null ? objectId.hashCode() : 0;
    }
}

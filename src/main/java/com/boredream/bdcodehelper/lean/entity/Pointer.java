package com.boredream.bdcodehelper.lean.entity;

import java.io.Serializable;

public class Pointer implements Serializable {
    public static final String TYPE = "Pointer";

    protected String __type;
    protected String className;
    protected String objectId;

    public Pointer() {
        this.__type = TYPE;
        this.className = getClass().getSimpleName();
    }

    public Pointer(String className, String objectId) {
        this.__type = TYPE;
        this.setClassName(className);
        this.setObjectId(objectId);
    }

    public Pointer getPointer() {
        Pointer pointer = new Pointer();
        pointer.setObjectId(getObjectId());
        return pointer;
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

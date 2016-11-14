package com.boredream.bdcodehelper.entity;

public class WhereQuery {

    private String key;
    private String keyClassName;
    private String queryKey;
    private String queryValue;

    public WhereQuery(String key, String keyClassName, String queryKey, String queryValue) {
        this.key = key;
        this.keyClassName = keyClassName;
        this.queryKey = queryKey;
        this.queryValue = queryValue;
    }

    public String getQueryString() {
        String where = "{\"%s\":{\"$inQuery\":{\"where\":{\"%s\":\"%s\"},\"className\":\"%s\"}}}";
        where = String.format(where, key, queryKey, queryValue, keyClassName);
        return where;
    }
}

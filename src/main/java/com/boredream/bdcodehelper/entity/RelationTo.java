package com.boredream.bdcodehelper.entity;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class RelationTo {
    public static final String OP_RELATEDTO = "$relatedTo";

    private String key;
    private Pointer object;

    public RelationTo(String key, Pointer object) {
        this.key = key;
        this.object = object;
    }

    public String getJsonStr() {
        Map<String, Object> mapRelationTo = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("object", object);
        mapRelationTo.put(OP_RELATEDTO, map);
        return new Gson().toJson(mapRelationTo);
    }
}

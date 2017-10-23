package com.boredream.bdcodehelper.lean.entity;

import java.util.ArrayList;

public class ListResponse<T> extends BaseResponse {

    private ArrayList<T> results;

    public ArrayList<T> getResults() {
        return results;
    }

    public void setResults(ArrayList<T> results) {
        this.results = results;
    }
}

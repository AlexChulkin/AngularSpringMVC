package com.somecode.domain;

import java.util.List;

/**
 * Created by alexc_000 on 2016-07-22.
 */
public class ComptsParams {
    private List<String> vals;
    private String label;
    private long id;

    public ComptsParams(List<String> vals, long id) {
        this.vals = vals;
        this.id = id;
    }

    public ComptsParams(List<String> vals, String label) {
        this.vals = vals;
        this.label = label;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getVals() {
        return vals;
    }

    public void setVals(List<String> vals) {
        this.vals = vals;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

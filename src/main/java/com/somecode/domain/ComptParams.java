package com.somecode.domain;

import java.util.List;

/**
 * Created by alexc_000 on 2016-07-22.
 */
public class ComptParams {
    private List<String> vals;
    private String label;
    private long id;

    public long getId() {
        return id;
    }

    public ComptParams setId(long id) {
        this.id = id;
        return this;
    }

    public List<String> getVals() {
        return vals;
    }

    public ComptParams setVals(List<String> vals) {
        this.vals = vals;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public ComptParams setLabel(String label) {
        this.label = label;
        return this;
    }
}

package com.somecode.domain;

import java.util.List;

/**
 * Created by alexc_000 on 2016-07-22.
 */
public class ComptsParams {
    private List<String> defaultVals;
    private String label;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getDefaultVals() {
        return defaultVals;
    }

    public void setDefaultVals(List<String> defaultVals) {
        this.defaultVals = defaultVals;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

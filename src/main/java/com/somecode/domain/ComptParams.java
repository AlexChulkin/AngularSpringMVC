package com.somecode.domain;

import java.util.List;

import static com.somecode.utils.Utils.getMessage;

/**
 * Created by alexc_000 on 2016-07-22.
 */
public class ComptParams implements SelfSettingEntityPrototype {
    private static final String STRING_VERSION = "comptParams.toString";

    private List<String> vals;
    private String label;
    private Long id;

    public Long getId() {
        return id;
    }

    public ComptParams setId(Long id) {
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

    @Override
    public String toString() {
        return getMessage(STRING_VERSION, new Object[]{id, label, vals});
    }
}

/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

import java.util.List;

import static com.somecode.utils.Utils.getMessage;

/**
 * The fairly self-explanatory class containing the info for the {@link Compt} save/update.
 * @version 1.0
 */
public class ComptParams implements SelfSettingEntityPrototype {
    private static final String STRING_VERSION = "comptParams.toString";

    /**
     * The checked {@link ComboData} labels associated with the given {@link Compt} entity
     */
    private List<String> vals;
    /**
     * The label of the given {@link Compt} entity
     */
    private String label;
    /**
     * The id of the given {@link Compt} entity
     */
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

    /**
     * Returns a brief description of an instance of this class.
     * The pattern is the following:
     * ComptParams with id = {@link #id} and label = {@link #label} and comboData values: {@link #vals}
     */
    @Override
    public String toString() {
        return getMessage(STRING_VERSION, new Object[]{id, label, vals});
    }
}

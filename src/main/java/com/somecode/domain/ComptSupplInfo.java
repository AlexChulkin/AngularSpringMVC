/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

import static com.somecode.utils.Utils.getMessage;

/**
 * The fairly self-explanatory {@link ComptSupplInfo} class containing the supplementary info
 * for the convenient {@link Compt} entities front-end representation.
 * @version 1.0
 */

public class ComptSupplInfo implements EntityProtoType {

    private static final String CHECKED_STRING_VERSION = "comptSupplInfo.checked.toString";
    private static final String UNCHECKED_STRING_VERSION = "comptSupplInfo.unchecked.toString";
    /**
     * The {@link Compt} id
     */
    private Long comptId;

    /**
     * The {@link DataCompt} state id,
     * {@link DataCompt} is connected to the {@link Compt} via the foreign key.
     */
    private Long stateId;
    /**
     * The {@link ComboData} label,
     * {@link ComboData} is connected to the {@link DataCompt} via the foreign key.
     */
    private String label;
    /** The {@link DataCompt} checked flag,
     *  {@link DataCompt} is connected to the {@link Compt} via the foreign key.
     */
    private boolean checked;

    public ComptSupplInfo() {
    }

    public ComptSupplInfo(Long comptId, Long stateId, String label, boolean checked) {
        this.comptId = comptId;
        this.stateId = stateId;
        this.label = label;
        this.checked = checked;
    }

    public Long getId() {
        return comptId;
    }

    public void setId(Long comptId) {
        this.comptId = comptId;
    }

    public Long getComptId() {
        return comptId;
    }

    public Long getStateId() {
        return stateId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChecked() {
        return checked;
    }

    /**
     * Returns a brief description of an instance of this class.
     * The pattern is the following:
     * ComptSupplInfo with compt Id: {link {@link #comptId}}, label: {@link #label}, state Id: {@link #stateId}
     * and (un)checked
     */
    @Override
    public String toString() {
        return getMessage(checked ? CHECKED_STRING_VERSION : UNCHECKED_STRING_VERSION,
                new Object[]{comptId, label, stateId});
    }

}


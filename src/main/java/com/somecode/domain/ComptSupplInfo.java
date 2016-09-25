package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-07-16.
 */

import static com.somecode.helper.Helper.getMessage;

public class ComptSupplInfo {

    private static final String CHECKED_STRING_VERSION = "comptSupplInfo.checked.toString";
    private static final String UNCHECKED_STRING_VERSION = "comptSupplInfo.unchecked.toString";

    private Long comptId;
    private Long stateId;
    private String label;
    private boolean checked;

    public ComptSupplInfo(Long comptId, Long stateId, String label, boolean checked) {
        this.comptId = comptId;
        this.stateId = stateId;
        this.label = label;
        this.checked = checked;
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

    @Override
    public String toString() {

        return getMessage(checked ? CHECKED_STRING_VERSION : UNCHECKED_STRING_VERSION,
                new Object[]{comptId, label, stateId});
    }

}


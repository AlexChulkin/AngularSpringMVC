package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-07-16.
 */

public class ComptSupplInfo implements HasLabel {
    private long comptId;
    private long stateId;
    private String label;
    private boolean checked;

    public ComptSupplInfo(long comptId, long stateId, String label, boolean checked){
        this.comptId = comptId;
        this.stateId = stateId;
        this.label = label;
        this.checked = checked;
    }

    public long getComptId() {
        return comptId;
    }


    public long getStateId() {
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

}


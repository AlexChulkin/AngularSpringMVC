package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-07-16.
 */

public class ComptSupplInfo implements HasLabel {
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
        return "ComptSupplInfo with compt Id: " + comptId + ", label: " + label + ", state Id: " + stateId + " and "
                + (checked ? "checked" : "not checked");
    }

}


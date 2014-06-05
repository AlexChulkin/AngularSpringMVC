package com.luxoft.snp.domain;

/**
 * Created by AChulkin on 09.04.14.
 */
public class PseudoData {
    private int dataComptId;
    private int comptId;
    private int stateId;
    private String label;
    private int checked;



    public PseudoData( int dataComptId, int comptId, int stateId, String label, int checked) {
        this.dataComptId = dataComptId;
        this.comptId = comptId;
        this.stateId = stateId;
        this.label = label;
        this.checked = checked;
    }

    public int getDataComptId() {
        return dataComptId;
    }

    public void setDataComptId(int dataComptId) {
        this.dataComptId = dataComptId;
    }

    public int getComptId() {
        return comptId;
    }

    public void setComptId(int comptId) {
        this.comptId = comptId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}

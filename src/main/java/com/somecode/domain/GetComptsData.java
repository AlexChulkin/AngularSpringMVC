package com.somecode.domain;

import javax.persistence.NamedQuery;

/**
 * Created by alexc_000 on 2016-07-16.
 */
@NamedQuery(name="GetComptsData.getComptsData",
        query="SELECT DISTINCT NEW com.somecode.domain.GetComptsData(c.id, dc.state.id, sd.label, dc.checked) " +
                "FROM Packet p JOIN p.compts c JOIN c.dataCompts dc JOIN dc.staticData sd " +
                "WHERE p.id = :packetId ORDER BY dc.id")
public class GetComptsData {
    private long comptId;
    private long stateId;
    private String label;
    private boolean checked;

    public GetComptsData(long comptId, long stateId, String label, boolean checked){
        this.comptId = comptId;
        this.stateId = stateId;
        this.label = label;
        this.checked = checked;
    }

    public long getComptId() {
        return comptId;
    }

    public void setComptId(long comptId) {
        this.comptId = comptId;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
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

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}


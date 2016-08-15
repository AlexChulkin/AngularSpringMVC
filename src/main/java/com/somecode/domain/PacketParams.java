package com.somecode.domain;

import java.util.List;

/**
 * Created by alexc_000 on 2016-08-13.
 */
public class PacketParams {
    private long id;
    private long stateId;
    private List<ComptParams> comptParamsList;

    public List<ComptParams> getComptParamsList() {
        return comptParamsList;
    }

    public void setComptParamsList(List<ComptParams> comptParamsList) {
        this.comptParamsList = comptParamsList;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

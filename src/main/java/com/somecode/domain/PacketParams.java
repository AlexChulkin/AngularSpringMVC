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

    public PacketParams setComptParamsList(List<ComptParams> comptParamsList) {
        this.comptParamsList = comptParamsList;
        return this;
    }

    public long getStateId() {
        return stateId;
    }

    public PacketParams setStateId(long stateId) {
        this.stateId = stateId;
        return this;
    }

    public long getId() {

        return id;
    }

    public PacketParams setId(long id) {
        this.id = id;
        return this;
    }
}

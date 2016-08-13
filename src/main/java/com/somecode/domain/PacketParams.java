package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-08-13.
 */
public class PacketParams {
    private long id;
    private long stateId;

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

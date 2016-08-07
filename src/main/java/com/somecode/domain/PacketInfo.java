package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-07-16.
 */


public class PacketInfo {
    private long id;
    private long stateId;

    public PacketInfo(long id, long stateId) {
        this.id = id;
        this.stateId = stateId;
    }

    public long getId() {
        return id;
    }

    public long getStateId() {
        return stateId;
    }
}

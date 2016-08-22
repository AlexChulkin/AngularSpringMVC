package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-07-16.
 */


public class PacketInfo {
    private long id;
    private long stateId;

    public PacketInfo(Packet packet) {
        this.id = packet.getId();
        this.stateId = packet.getState().getId();
    }

    public long getId() {
        return id;
    }

    public long getStateId() {
        return stateId;
    }

    @Override
    public String toString() {
        return "Packet Info with id: " + id + " and state Id: " + stateId;
    }

}

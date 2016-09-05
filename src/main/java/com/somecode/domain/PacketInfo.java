package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-07-16.
 */


public class PacketInfo {
    private Long id;
    private Long stateId;

    public PacketInfo(Packet packet) {
        this.id = packet.getId();
        this.stateId = packet.getState().getId();
    }

    public Long getId() {
        return id;
    }

    public Long getStateId() {
        return stateId;
    }

    @Override
    public String toString() {
        return "Packet Info with id: " + id + " and state Id: " + stateId;
    }

}

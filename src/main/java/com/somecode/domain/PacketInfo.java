package com.somecode.domain;

import static com.somecode.utils.Utils.getMessage;
/**
 * Created by alexc_000 on 2016-07-16.
 */


public class PacketInfo implements EntityProtoType {
    private static final String STRING_VERSION = "packetInfo.toString";

    private Long id;
    private Long stateId;

    public PacketInfo() {
    }

    public PacketInfo(Packet packet) {
        this.id = packet.getId();
        this.stateId = packet.getState().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStateId() {
        return stateId;
    }

    @Override
    public String toString() {
        return getMessage(STRING_VERSION, new Object[]{id, stateId});
    }

}

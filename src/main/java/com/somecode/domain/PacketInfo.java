/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

import static com.somecode.utils.Utils.getMessage;

/**
 * The self-explanatory {@link PacketInfo} POJO class: the brief version of the {@link Compt} entity.
 * @version 1.0
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

    /**
     * Returns a brief description of an instance of this class.
     * The pattern is the following:
     * Packet Info with id: {@link #id} and state Id: {@link #stateId}
     */
    @Override
    public String toString() {
        return getMessage(STRING_VERSION, new Object[]{id, stateId});
    }

}

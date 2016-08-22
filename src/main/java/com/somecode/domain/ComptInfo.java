package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-07-16.
 */


public class ComptInfo {
    private long id;
    private String label;
    private long packetId;

    public ComptInfo(Compt compt) {
        this.id = compt.getId();
        this.label = compt.getLabel();
        this.packetId = compt.getPacket().getId();
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public long getPacketId() {
        return packetId;
    }

    @Override
    public String toString() {
        return "Compt Info with id: " + id + ", label: " + label + " and packet Id " + packetId;
    }
}

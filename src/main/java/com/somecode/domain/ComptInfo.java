package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-07-16.
 */


public class ComptInfo {
    private long id;
    private String label;
    private long packetId;

    public ComptInfo(long id, String label, long packetId) {
        this.id = id;
        this.label = label;
        this.packetId = packetId;
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
}

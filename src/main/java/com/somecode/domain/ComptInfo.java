/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

/**
 * The self-explanatory {@link ComptInfo} POJO class: the brief version of the {@link Compt} entity.
 * @version 1.0
 */

import static com.somecode.utils.Utils.getMessage;

public class ComptInfo implements EntityProtoType {
    private final static String STRING_VERSION = "comptInfo.toString";

    private long id;
    private String label;
    private long packetId;

    public ComptInfo() {
    }

    public ComptInfo(Compt compt) {
        this.id = compt.getId();
        this.label = compt.getLabel();
        this.packetId = compt.getPacket().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public long getPacketId() {
        return packetId;
    }

    @Override
    public String toString() {
        return getMessage(STRING_VERSION, new Object[]{id, label, packetId});
    }
}

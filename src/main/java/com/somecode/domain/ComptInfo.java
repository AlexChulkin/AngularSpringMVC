package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-07-16.
 */

import static com.somecode.helper.Helper.getMessage;

public class ComptInfo {
    private final static String STRING_VERSION = "comptInfo.toString";

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
        return getMessage(STRING_VERSION, new Object[]{id, label, packetId});
    }
}

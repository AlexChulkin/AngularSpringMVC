package com.somecode.domain;

/**
 * Created by vharutyunyan on 20.06.2014.
 */
public class Params {

    private long[] idsToRemove;
    private String comptLabel;
    private long comptId;
    private long packetId;
    private String[] defaultVals;

    public String getComptLabel() {
        return comptLabel;
    }

    public void setComptLabel(String comptLabel) {
        this.comptLabel = comptLabel;
    }

    public long getComptId() {
        return comptId;
    }

    public void setComptId(long comptId) {
        this.comptId = comptId;
    }

    public long getPacketId() {
        return packetId;
    }

    public void setPacketId(long packetId) {
        this.packetId = packetId;
    }

    public String[] getDefaultVals() {
        return defaultVals;
    }

    public void setDefaultVals(String[] defaultVals) {
        this.defaultVals = defaultVals;
    }

    public long[] getIdsToRemove() {
        return idsToRemove;
    }

    public void setIdsToRemove(long[] idsToRemove) {
        this.idsToRemove = idsToRemove;
    }
}

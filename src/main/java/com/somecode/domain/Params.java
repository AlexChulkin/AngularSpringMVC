package com.somecode.domain;

/**
 * Created by vharutyunyan on 20.06.2014.
 */
public class Params {

    private Long[] idsToRemove;
    private String comptLabel;
    private long comptId;
    private long packetId;
    private long newStateId;
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

    public Long[] getIdsToRemove() {
        return idsToRemove;
    }

    public void setIdsToRemove(Long[] idsToRemove) {
        this.idsToRemove = idsToRemove;
    }

    public long getNewStateId() {
        return newStateId;
    }

    public void setNewStateId(long newStateId) {
        this.newStateId = newStateId;
    }
}

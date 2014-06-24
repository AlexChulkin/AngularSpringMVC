package com.luxoft.snp.domain;

/**
 * Created by vharutyunyan on 20.06.2014.
 */
public class Params {

    private int[] idsToRemove;
    private String comptLabel;
    private int comptId;
    private int packetId;
    private String[] defaultVals;

    public String getComptLabel() {
        return comptLabel;
    }

    public void setComptLabel(String comptLabel) {
        this.comptLabel = comptLabel;
    }

    public int getComptId() {
        return comptId;
    }

    public void setComptId(int comptId) {
        this.comptId = comptId;
    }

    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public String[] getDefaultVals() {
        return defaultVals;
    }

    public void setDefaultVals(String[] defaultVals) {
        this.defaultVals = defaultVals;
    }

    public int[] getIdsToRemove() {
        return idsToRemove;
    }

    public void setIdsToRemove(int[] idsToRemove) {
        this.idsToRemove = idsToRemove;
    }
}

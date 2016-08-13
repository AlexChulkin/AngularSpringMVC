package com.somecode.domain;

import java.util.List;

/**
 * Created by vharutyunyan on 20.06.2014.
 */
public class Params {

    private List<Long> comptIds;
    private List<Long> packetIds;
    private long comptId;
    private long packetId;
    private long newStateId;
    private List<ComptParams> comptParamsList;
    private List<String> defaultVals;
    private List<PacketParams> updatedPackets;

    public List<Long> getPacketIds() {
        return packetIds;
    }

    public void setPacketIds(List<Long> packetIds) {
        this.packetIds = packetIds;
    }

    public List<PacketParams> getUpdatedPackets() {
        return updatedPackets;
    }

    public void setUpdatedPackets(List<PacketParams> updatedPackets) {
        this.updatedPackets = updatedPackets;
    }

    public List<String> getDefaultVals() {
        return defaultVals;
    }

    public void setDefaultVals(List<String> defaultVals) {
        this.defaultVals = defaultVals;
    }

    public List<ComptParams> getComptParamsList() {
        return comptParamsList;
    }

    public void setComptParamsList(List<ComptParams> comptParamsList) {
        this.comptParamsList = comptParamsList;
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


    public List<Long> getComptIds() {
        return comptIds;
    }

    public void setComptIds(List<Long> comptIds) {
        this.comptIds = comptIds;
    }

    public long getNewStateId() {
        return newStateId;
    }

    public void setNewStateId(long newStateId) {
        this.newStateId = newStateId;
    }
}

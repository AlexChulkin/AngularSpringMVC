package com.somecode.domain;

import java.util.List;

/**
 * Created by vharutyunyan on 20.06.2014.
 */
public class Params {

    private List<String> idsToRemove;
    private long comptId;
    private long packetId;
    private long newStateId;
    private List<ComptsParams> comptsParamsList;
    private List<String> defaultVals;

    public List<String> getDefaultVals() {
        return defaultVals;
    }

    public void setDefaultVals(List<String> defaultVals) {
        this.defaultVals = defaultVals;
    }

    public List<ComptsParams> getComptsParamsList() {
        return comptsParamsList;
    }

    public void setComptsParamsList(List<ComptsParams> comptsParamsList) {
        this.comptsParamsList = comptsParamsList;
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


    public List<String> getIdsToRemove() {
        return idsToRemove;
    }

    public void setIdsToRemove(List<String> idsToRemove) {
        this.idsToRemove = idsToRemove;
    }

    public long getNewStateId() {
        return newStateId;
    }

    public void setNewStateId(long newStateId) {
        this.newStateId = newStateId;
    }
}

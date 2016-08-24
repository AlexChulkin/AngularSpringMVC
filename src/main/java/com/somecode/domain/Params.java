package com.somecode.domain;

import java.util.List;

/**
 * Created by vharutyunyan on 20.06.2014.
 */
public class Params {

    private List<Long> comptIdsToDelete;
    private List<Long> packetIdsToDelete;
    private List<ComptParams> comptsToUpdateParamsList;
    private List<PacketParams> packetsToAddParamsList;
    private List<PacketParams> packetsToUpdateParamsList;
    private Long packetId;


    public List<PacketParams> getPacketsToAddParamsList() {
        return packetsToAddParamsList;
    }

    public void setPacketsToAddParamsList(List<PacketParams> packetsToAddParamsList) {
        this.packetsToAddParamsList = packetsToAddParamsList;
    }

    public List<PacketParams> getPacketsToUpdateParamsList() {
        return packetsToUpdateParamsList;
    }

    public void setPacketsToUpdateParamsList(List<PacketParams> packetsToUpdateParamsList) {
        this.packetsToUpdateParamsList = packetsToUpdateParamsList;
    }

    public List<Long> getPacketIdsToDelete() {
        return packetIdsToDelete;
    }

    public void setPacketIdsToDelete(List<Long> packetIdsToDelete) {
        this.packetIdsToDelete = packetIdsToDelete;
    }

    public List<ComptParams> getComptsToUpdateParamsList() {
        return comptsToUpdateParamsList;
    }

    public void setComptsToUpdateParamsList(List<ComptParams> comptsToUpdateParamsList) {
        this.comptsToUpdateParamsList = comptsToUpdateParamsList;
    }

    public Long getPacketId() {
        return packetId;
    }

    public void setPacketId(Long packetId) {
        this.packetId = packetId;
    }


    public List<Long> getComptIdsToDelete() {
        return comptIdsToDelete;
    }

    public void setComptIdsToDelete(List<Long> comptIdsToDelete) {
        this.comptIdsToDelete = comptIdsToDelete;
    }
}

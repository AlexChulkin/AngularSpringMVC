/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

import java.util.List;

/**
 * The fairly self-explanatory {@link DataParams} POJO class containing the lists of different entities and ids.
 * This class is used for the passing of the data that is going to be persisted to the DB from the front-end to
 * the back-end.
 */
public class DataParams {

    /**
     * List of the {@link Compt} ids set for removal
     */
    private List<Long> comptIdsToDelete;
    /**
     * List of the {@link Packet} ids set for removal
     */
    private List<Long> packetIdsToDelete;
    /**
     * List of the {@link ComptParams} instances representing the {@link Compt}s set for removal
     */
    private List<ComptParams> comptsToUpdateParamsList;
    /** List of the {@link PacketParams} instances representing the {@link Packet}s set for adding to the DB */
    private List<PacketParams> packetsToAddParamsList;
    /** List of the {@link PacketParams} instances representing the {@link Packet}s set for update in the DB */
    private List<PacketParams> packetsToUpdateParamsList;
    /** The {@link Packet} id */
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

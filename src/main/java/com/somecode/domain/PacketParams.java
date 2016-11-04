/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

import java.util.List;

import static com.somecode.utils.Utils.getMessage;

/**
 * The fairly self-explanatory class containing the info for the {@link Packet} save/update.
 */

public class PacketParams implements SelfSettingEntityPrototype {
    private static final String STRING_VERSION = "packetParams.toString";
    /**
     * The {@link Packet} id, null if it's the new {@link Packet} saving, not-null if it's the {@link Packet}
     * update
     */
    private Long id;
    /**
     * The {@link Packet}'s {@link State} id
     */
    private Long stateId;
    /**
     * The list of the {@link ComptParams} representing the new {@link Compt}s
     */
    private List<ComptParams> newComptParamsList;

    public List<ComptParams> getComptParamsList() {
        return newComptParamsList;
    }

    public PacketParams setNewComptParamsList(List<ComptParams> newComptParamsList) {
        this.newComptParamsList = newComptParamsList;
        return this;
    }

    public Long getStateId() {
        return stateId;
    }

    public PacketParams setStateId(Long stateId) {
        this.stateId = stateId;
        return this;
    }

    public Long getId() {

        return id;
    }

    public PacketParams setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return getMessage(STRING_VERSION, new Object[]{id, stateId, newComptParamsList});
    }
}

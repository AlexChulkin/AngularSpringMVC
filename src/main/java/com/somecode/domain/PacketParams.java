package com.somecode.domain;

import java.util.List;

import static com.somecode.helper.Helper.getMessage;

/**
 * Created by alexc_000 on 2016-08-13.
 */
public class PacketParams {
    private static final String STRING_VERSION = "packetParams.toString";
    private Long id;
    private Long stateId;
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

package com.somecode.domain;

import java.util.List;

/**
 * Created by alexc_000 on 2016-08-13.
 */
public class PacketParams {
    private Long id;
    private Long stateId;
    private List<ComptParams> updatedComptParamsList;
    private List<ComptParams> addedComptParamsList;

    public List<ComptParams> getUpdatedComptParamsList() {
        return updatedComptParamsList;
    }

    public PacketParams setUpdatedComptParamsList(List<ComptParams> updatedComptParamsList) {
        this.updatedComptParamsList = updatedComptParamsList;
        return this;
    }

    public List<ComptParams> getAddedComptParamsList() {
        return addedComptParamsList;
    }

    public PacketParams setAddedComptParamsList(List<ComptParams> addedComptParamsList) {
        this.addedComptParamsList = addedComptParamsList;
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
}

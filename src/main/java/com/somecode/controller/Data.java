/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.controller;

import com.somecode.domain.*;

import java.util.Collections;
import java.util.List;

/**
 * The self-explanatory immutable {@link Data} class containing the lists of different entities loaded from the DB as
 * a result of the front-end reques.
 * @version 1.0
 */
public class Data {
    private List<ComptInfo> compts;
    private List<ComptSupplInfo> comptSupplInfo;
    private List<PacketInfo> packets;
    private List<State> states;
    private List<ComboData> comboData;

    Data(Builder builder) {
        this.comboData = Collections.unmodifiableList(builder.comboData);
        this.states = Collections.unmodifiableList(builder.states);
        this.packets = Collections.unmodifiableList(builder.packets);
        this.comptSupplInfo = Collections.unmodifiableList(builder.comptSupplInfo);
        this.compts = Collections.unmodifiableList(builder.compts);
    }

    static Builder createBuilder() {
        return new Builder();
    }

    List<ComptInfo> getCompts() {
        return compts;
    }

    List<ComptSupplInfo> getComptSupplInfo() {
        return comptSupplInfo;
    }

    List<PacketInfo> getPackets() {
        return packets;
    }

    List<State> getStates() {
        return states;
    }

    List<ComboData> getComboData() {
        return comboData;
    }

    static class Builder {
        private List<ComptInfo> compts;
        private List<ComptSupplInfo> comptSupplInfo;
        private List<PacketInfo> packets;
        private List<State> states;
        private List<ComboData> comboData;

        Builder compts(List<ComptInfo> val) {
            compts = val;
            return this;
        }

        Builder comboData(List<ComboData> val) {
            comboData = val;
            return this;
        }

        Builder comptSupplInfo(List<ComptSupplInfo> val) {
            comptSupplInfo = val;
            return this;
        }

        Builder packets(List<PacketInfo> val) {
            packets = val;
            return this;
        }

        Builder states(List<State> val) {
            states = val;
            return this;
        }

        Data build() {
            return new Data(this);
        }
    }
}

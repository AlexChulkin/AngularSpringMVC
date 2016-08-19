package com.somecode.domain;

import java.util.List;

/**
 * Created by alexc_000 on 2016-08-19.
 */
public class Data {
    private List<ComptInfo> compts;
    private List<ComptSupplInfo> comptSupplInfo;
    private List<PacketInfo> packets;
    private List<State> states;
    private List<ComboData> comboData;

    public List<ComptInfo> getCompts() {
        return compts;
    }

    public Data setCompts(List<ComptInfo> compts) {
        this.compts = compts;
        return this;
    }

    public List<ComptSupplInfo> getComptSupplInfo() {
        return comptSupplInfo;
    }

    public Data setComptSupplInfo(List<ComptSupplInfo> comptSupplInfo) {
        this.comptSupplInfo = comptSupplInfo;
        return this;
    }

    public List<PacketInfo> getPackets() {
        return packets;
    }

    public Data setPackets(List<PacketInfo> packets) {
        this.packets = packets;
        return this;
    }

    public List<State> getStates() {
        return states;
    }

    public Data setStates(List<State> states) {
        this.states = states;
        return this;
    }

    public List<ComboData> getComboData() {
        return comboData;
    }

    public Data setComboData(List<ComboData> comboData) {
        this.comboData = comboData;
        return this;
    }


}

package com.somecode.dao;

import com.somecode.domain.*;

import java.util.List;

/**
 * Created by alexc_000 on 2016-06-24.
 */
public interface ComptDao {
    List<ComptSupplInfo> getComptsSupplInfo(long packetId);
    Packet getPacket(long packetId);
    List<State> getStates();
    List<ComptInfo> getCompts(long packetId);

    void updateCompts(List<ComptsParams> comptsParamsList);
    void updatePacketsState(long packetId, long newStateId);

    void removeCompts(List<String> idsToRemove);

    void addCompts(long packetId, List<ComptsParams> comptsParamsList);

    List<ComboData> getDefaultComboData();
}

package com.somecode.dao;

import com.somecode.domain.*;

import java.util.List;

/**
 * Created by alexc_000 on 2016-06-24.
 */
public interface ComptDao {
    List<ComptSupplInfo> getComptsSupplInfo(long packetId);

    Long getPacketStateId(long packetId);
    List<State> getStates();
    List<ComptInfo> getCompts(long packetId);

    List<Long> updateCompts(List<ComptsParams> comptsParamsList);

    Long updatePacketState(long packetId, long newStateId);

    List<Long> removeCompts(List<Long> idsToRemove);

    List<Long> addCompts(long packetId, List<ComptsParams> comptsParamsList);
    List<ComboData> getDefaultComboData();
}

package com.somecode.dao;

import com.somecode.domain.*;

import java.util.List;

/**
 * Created by alexc_000 on 2016-06-24.
 */
public interface ComptDao {
    List<ComptSupplInfo> getComptsSupplInfoByPacketId(long packetId);

    List<ComptSupplInfo> getAllComptsSupplInfo();

    List<PacketInfo> getAllPackets();
    Long getPacketStateId(long packetId);

    List<State> getAllStates();

    List<ComptInfo> getComptsByPacketId(long packetId);

    List<ComptInfo> getAllCompts();
    List<Long> updateCompts(List<ComptsParams> comptsParamsList);
    Long updatePacketState(long packetId, long newStateId);
    List<Long> removeCompts(List<Long> idsToRemove);
    List<Long> addCompts(long packetId, List<ComptsParams> comptsParamsList);

    List<ComboData> getAllComboData();
}

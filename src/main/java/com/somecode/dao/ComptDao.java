package com.somecode.dao;

import com.somecode.domain.*;

import java.util.List;
import java.util.Map;

/**
 * Created by alexc_000 on 2016-06-24.
 */
public interface ComptDao {
    List<ComptSupplInfo> loadComptsSupplInfo(Long packetId);

    List<PacketInfo> loadPackets(Long packetId);

    List<State> loadAllStates() throws DatabaseException;

    List<ComptInfo> loadCompts(Long packetId);

    List<Long> deleteCompts(List<Long> idsToRemove);

    List<Long> deletePackets(List<Long> packetIdsToDelete);

    List<ComboData> loadAllComboData() throws DatabaseException;

    Map<Long, List<Long>> updateCompts(List<ComptParams> comptParamsList) throws DatabaseException;

    void addOrUpdatePackets(List<PacketParams> packetParamsList, OperationType operationType)
            throws DatabaseException;
}

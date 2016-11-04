/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.dao;

import com.somecode.domain.*;

import java.util.List;
import java.util.Map;

/**
 * The dao interface.
 */
public interface PacketAppDao {

    Role getUserRole(String username, String password);

    List<ComptSupplInfo> loadComptsSupplInfo(Long packetId);

    List<PacketInfo> loadPackets(Long packetId);

    List<State> loadAllStates() throws DatabaseException;

    List<ComptInfo> loadCompts(Long packetId);

    List<ComboData> loadAllComboData() throws DatabaseException;

    List<Long> deleteCompts(List<Long> idsToRemove);

    List<Long> deletePackets(List<Long> packetIdsToDelete);

    Map<Long, List<Long>> updateCompts(List<ComptParams> comptParamsList) throws DatabaseException;

    void addOrUpdatePackets(List<PacketParams> packetParamsList, OperationType operationType)
            throws DatabaseException;
}

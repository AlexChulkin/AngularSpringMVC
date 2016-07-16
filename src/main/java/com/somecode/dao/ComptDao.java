package com.somecode.dao;

import com.somecode.domain.ComptInfo;
import com.somecode.domain.GetComptsData;
import com.somecode.domain.Packet;
import com.somecode.domain.State;

import java.util.List;

/**
 * Created by alexc_000 on 2016-06-24.
 */
public interface ComptDao {
    List<GetComptsData> getComptsData(long packetId);
    Packet getPacket(long packetId);
    List<State> getStates();
    List<ComptInfo> getCompts(long packetId);
    void updateCompt(long comptId, String[] defaultVals);
    void removeCompts(Long[] idsToRemove);
    void addCompt(String label, long packetId, String[] defaultVals);
}

package com.somecode.dao;

import com.somecode.domain.Compt;
import com.somecode.domain.Packet;
import com.somecode.domain.State;

import java.util.List;

/**
 * Created by alexc_000 on 2016-06-24.
 */
public interface ComptDao {
    List<Object[]> getStaticData(long packetId);
    Packet getPacket(long packetId);
    List<State> getStates();
    List<Compt> getComponents(long packetId);
    void updateCompt(int comptId, String[] defaultVals);
    void removeCompts(int[] idsToRemove);
    void addCompt(String label, int packetId, String[] defaultVals);
}

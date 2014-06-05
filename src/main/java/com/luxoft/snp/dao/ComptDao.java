package com.luxoft.snp.dao;

import java.util.List;
import com.luxoft.snp.domain.*;

/**
 * Created by achulkin on 05.06.14.
 */
public interface ComptDao {
    List<Compt> getComponents(int packetId);
    List<PseudoData> getStaticData(int packetId);
    Integer getPacketState(int packetId);
    List<State> getStates();
    void deleteCompt(int id);
    Compt saveCompt(Compt compt);


}

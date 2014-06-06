package com.luxoft.snp.dao;

import com.luxoft.snp.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository("comptDao")
public class ComptDaoImpl implements ComptDao {

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<Compt> getComponents(int packetId){
        return em.createQuery("select compt from Compt  where compt.packet.id=:packetId order by compt.id")
                .setParameter("packetId",packetId).getResultList();
    }
    @SuppressWarnings("unchecked")
    public List<PseudoData> getStaticData(int packetId){
        List<Object[]> tempList =  em.createQuery("select sd.label, compt.id, dc.id, dc.state.id, dc.checked from StaticData sd, Compt compt, DataCompt dc" +
                " where compt.packet.id = :packetId and dc.compt = compt and sd = dc.staticData order by compt.id, dc.state")
                .setParameter("packetId",packetId).getResultList();
        List<PseudoData> returnList = new ArrayList<PseudoData>();
        for(Object[] o : tempList){
            returnList.add(new PseudoData((Integer)o[2],(Integer)o[1], (Integer)o[3], ((String)o[0]), (Integer)o[4]));
        }
        return returnList;
    }

    @SuppressWarnings("unchecked")
    public Integer getPacketState(int packetId) {
        List<Packet> returnList =  em.createQuery("select p from Packet where p.id=:packetId")
                .setParameter("packetId",packetId).getResultList();

        return (returnList!=null && !returnList.isEmpty()) ? (returnList.get(0)).getState() : null;
    }

    @SuppressWarnings("unchecked")
    public List<State> getStates(){
        return em.createQuery("select s from State").getResultList();

    }

}

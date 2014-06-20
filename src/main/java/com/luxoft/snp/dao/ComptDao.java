package com.luxoft.snp.dao;

import com.luxoft.snp.domain.Compt;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;



import com.luxoft.snp.domain.State;


import org.springframework.transaction.annotation.Transactional;

@Repository
public class ComptDao {

    @PersistenceContext
    private EntityManager em;


    @SuppressWarnings("unchecked")
    public List<Object[]> getStaticData(int packetId){
        return em.createQuery("select dc.id , compt.id , dc.state.id , sd.label , dc.checked  from StaticData sd, Compt compt, DataCompt dc" +
                " where compt.packet.id = :packetId and dc.compt.id = compt.id and sd.id = dc.staticData.id order by dc.id")
                .setParameter("packetId",packetId).getResultList();
    }

    @SuppressWarnings("unchecked")
    public Integer getPacketState(int packetId) {
        List<Integer> returnList =  em.createQuery("select s.id from Packet p, State s where p.id=:packetId and p.state.id=s.id")
                .setParameter("packetId",packetId).getResultList();

        return (returnList!=null && !returnList.isEmpty()) ? returnList.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly=true)
    public List<State> getStates(){
        return em.createQuery("select distinct s from State s order by s.id", State.class).getResultList();

    }




    @Transactional(readOnly=true)
    @SuppressWarnings("unchecked")
    public List<Compt> getComponents(int packetId){
        return em.createQuery("select compt from Compt compt where compt.packet.id=:packetId")
                .setParameter("packetId", packetId).getResultList();
    }
}

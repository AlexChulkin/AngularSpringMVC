package com.luxoft.snp.dao;

import com.luxoft.snp.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;


import org.springframework.transaction.annotation.Transactional;

@Repository
public class ComptDao {
    private  List<StaticData> defaultStaticData ;

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    private List<StaticData> getDefaultStaticData(){
        return em.createQuery("select  sd  from StaticData sd  order by sd.id").getResultList();
    }


    @SuppressWarnings("unchecked")
    public List<Object[]> getStaticData(int packetId){
        return em.createQuery("select dc.id , compt.id , dc.state.id , sd.label , dc.checked  from StaticData sd, Compt compt, DataCompt dc" +
                " where compt.packet.id = :packetId and dc.compt.id = compt.id and sd.id = dc.staticData.id order by dc.id")
                .setParameter("packetId",packetId).getResultList();
    }

    @SuppressWarnings("unchecked")
    public Packet getPacket(int packetId) {
        List<Packet> packetList =  em.createQuery("select p from Packet p where p.id=:packetId")
                .setParameter("packetId",packetId).getResultList();
        if(packetList!=null && !packetList.isEmpty()){
            return packetList.get(0);
        }
        return null;
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

    @Transactional(readOnly=true)
    @SuppressWarnings("unchecked")
    private List<DataCompt> getDataCompts(int comptId){
        return  em.createQuery("select dc from DataCompt dc where dc.compt.id=:comptId")
                .setParameter("comptId", comptId).getResultList();
    }

    @SuppressWarnings("unchecked")
    private int[] getDefaultIndeces(String[] defaultVals) {

        int[] defaultIndeces;
        defaultIndeces = new int[defaultVals.length];
        for(int i=0;i<defaultIndeces.length;i++) {
            defaultIndeces[i]=-1;
        }
        if(defaultStaticData==null) {
            defaultStaticData = getDefaultStaticData();
        }

        for (int j=0;j<defaultVals.length;j++) {
            for (int i=0; i<defaultStaticData.size(); i++) {
                if(defaultVals[j].equals(defaultStaticData.get(i).getLabel())){
                    defaultIndeces[j] = i;
                    break;
                }
            }
        }
        return defaultIndeces;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public void updateCompt(int comptId, String[] defaultVals) {
        int[] defaultIndeces = getDefaultIndeces( defaultVals);
        List<DataCompt> dataComptsList = getDataCompts(comptId);

        for(DataCompt dc : dataComptsList){
            if(defaultIndeces[dc.getState().getId()-1]==-1 && dc.getChecked()!=0
                    || defaultIndeces[dc.getState().getId()-1]!=-1 && dc.getChecked()==0){
                dc.setChecked(1-dc.getChecked());
                em.merge(dc);
            }
        }
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public void removeCompts(String idsToRemove) {
        em.createQuery("DELETE from Compt c where c.id in (:ids)")
                .setParameter("ids",idsToRemove)
                .executeUpdate();
    }
    @Transactional
    @SuppressWarnings("unchecked")
    public void addCompt(String label, int packetId, String[] defaultVals) {

        int[] defaultIndeces = getDefaultIndeces(defaultVals);
        Compt newCompt = new Compt();
        newCompt.setLabel(label);
        newCompt.setPacket(getPacket(packetId));
        em.merge(newCompt);

        List<State> statesList = getStates();
        if(defaultStaticData==null) {
            defaultStaticData = getDefaultStaticData();
        }
        for(int j=0; j<statesList.size(); j++) {
            for(int i=0; i<defaultStaticData.size();i++) {
                DataCompt dc = new DataCompt();
                dc.setCompt(newCompt);
                dc.setState(statesList.get(j));
                dc.setStaticData(defaultStaticData.get(i));
                dc.setChecked(defaultIndeces[j]==i?1:0);
                em.merge(dc);
            }
        }

    }
}

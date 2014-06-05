package com.luxoft.snp.dao;

import com.luxoft.snp.domain.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("comptDao")
public class ComptDao {

    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public List<Compt> getComponents(int packetId){
        return sessionFactory.getCurrentSession().createQuery("from Compt as compt where compt.packet.id=:packetId order by compt.id")
                .setParameter("packetId",packetId).list();
    }
    @SuppressWarnings("unchecked")
    public List<PseudoData> getStaticData(int packetId){
        List<Object[]> tempList =  sessionFactory.getCurrentSession().createQuery("from StaticData as sd, Compt as compt, DataCompt as dc" +
                " where compt.packet.id = :packetId and dc.compt = compt and sd = dc.staticData order by compt.id, dc.state")
                .setParameter("packetId",packetId).list();
        List<PseudoData> returnList = new ArrayList<PseudoData>();
        for(Object[] o : tempList){
            DataCompt dc = (DataCompt) o[2];
            returnList.add(new PseudoData(dc.getId(),((Compt)o[1]).getId(), dc.getState().getId(), ((StaticData)o[0]).getLabel(), dc.getChecked()));
        }
        return returnList;
    }

    public Integer getPacketState(int packetId) {
        List<Packet> returnList =  sessionFactory.getCurrentSession().createQuery("from Packet as p where p.id=:packetId")
                .setParameter("packetId",packetId).list();

        return (returnList!=null && !returnList.isEmpty()) ? ((Packet)returnList.get(0)).getState() : null;
    }

    public List<State> getStates(){
        return  sessionFactory.getCurrentSession().createQuery("from State as s").list();

    }
    /**
     * Delete a compt with the id passed as parameter
     * @param id
     */
    public void deleteCompt(int id){
        Object record = sessionFactory.getCurrentSession().load(Compt.class, id);
        sessionFactory.getCurrentSession().delete(record);
    }

    /**
     * Create a new compt on the database or
     * Update compt
     * @param compt
     * @return contact added or updated in DB
     */
    public Compt saveCompt(Compt compt){
        sessionFactory.getCurrentSession().saveOrUpdate(compt);
        return compt;
    }
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}

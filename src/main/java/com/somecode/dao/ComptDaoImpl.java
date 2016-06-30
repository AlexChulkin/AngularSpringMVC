package com.somecode.dao;

import com.google.common.collect.Lists;
import com.somecode.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;



import org.springframework.transaction.annotation.Transactional;

@Repository
public class ComptDaoImpl implements  ComptDao {
    private static final Logger LOGGER = Logger.getLogger(ComptDaoImpl.class);

    private  List<StaticData> defaultStaticData ;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private StaticDataRepository staticDataRepository;

    @Autowired
    private PacketRepository packetRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private ComptRepository comptRepository;

    @PostConstruct
    @Transactional(readOnly = true)
    private void setDefaultStaticData(){
        defaultStaticData = Lists.newArrayList(staticDataRepository.findAllByOrderByIdAsc());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> getStaticData(long packetId){
        return em.createQuery("select dc.id , compt.id , dc.state.id , sd.label , dc.checked  from StaticData sd, Compt compt, DataCompt dc" +
                " where compt.packet.id = :packetId and dc.compt.id = compt.id and sd.id = dc.staticData.id order by dc.id")
                .setParameter("packetId",packetId).getResultList();
    }

    @Override
    public Packet getPacket(long packetId) {
        return packetRepository.findOne(packetId);
    }

    @Override
    @Transactional(readOnly=true)
    public List<State> getStates(){
        return Lists.newArrayList(stateRepository.findAll());
    }

    @Override
    @Transactional(readOnly=true)
    public List<Compt> getComponents(long packetId){
        return comptRepository.findByPacket_id(packetId);
    }

    @Transactional(readOnly=true)
    @SuppressWarnings("unchecked")
    private List<DataCompt> getDataCompts(int comptId){
        return  em.createQuery("select dc from DataCompt dc where dc.compt.id=:comptId")
                .setParameter("comptId", comptId).getResultList();
    }

    @SuppressWarnings("unchecked")
    private int[] getDefaultIndeces(String[] defaultVals) {

        LOGGER.info("defaultVals: " + Arrays.toString(defaultVals));
        int[] defaultIndeces;
        defaultIndeces = new int[defaultVals.length];
        for(int i=0;i<defaultIndeces.length;i++) {
            defaultIndeces[i]=-1;
        }

        for (int j=0;j<defaultVals.length;j++) {
            for (int i=0; i<defaultStaticData.size(); i++) {
                LOGGER.info("defaultVals j : " +defaultVals[j]);
                LOGGER.info("defaultStaticData.get(i).getLabel() : " +defaultStaticData.get(i).getLabel());

                if(defaultVals[j].equals(defaultStaticData.get(i).getLabel())){
                    defaultIndeces[j] = i;
                    break;
                }
            }
        }
        LOGGER.info("defaultIndeces: " + Arrays.toString(defaultIndeces));
        return defaultIndeces;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public void updateCompt(int comptId, String[] defaultVals) {
        int[] defaultIndeces = getDefaultIndeces( defaultVals);
        List<DataCompt> dataComptsList = getDataCompts(comptId);
        for(DataCompt dc : dataComptsList){
            if(defaultIndeces[dc.getState().getId()-1]==(dc.getStaticData().getId()-1) && dc.getChecked()==0
                    || defaultIndeces[dc.getState().getId()-1]!=(dc.getStaticData().getId()-1) && dc.getChecked()==1){
                dc.setChecked(1-dc.getChecked());
                em.merge(dc);
                LOGGER.info("update: "+dc.getId());

            }
        }
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public void removeCompts(int[] idsToRemove) {
        LOGGER.info("toremove: "+Arrays.toString(idsToRemove));
        String idsString="";
        for(int idToR : idsToRemove) {
            idsString = idsString+idToR+",";
        }
        idsString = idsString.substring(0,idsString.length()-1);
        em.createQuery("DELETE from DataCompt dc where dc.compt.id in ("+idsString+")")
                .executeUpdate();
        em.createQuery("DELETE from Compt c where c.id in (" + idsString + ")")
                .executeUpdate();
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public void addCompt(String label, int packetId, String[] defaultVals) {
        int[] defaultIndeces = getDefaultIndeces(defaultVals);
        Compt newCompt = new Compt();
        newCompt.setLabel(label);
        newCompt.setPacket(getPacket(packetId));
        newCompt = em.merge(newCompt);
        LOGGER.info("new Compt: "+newCompt);

        List<State> statesList = getStates();

        for(int j=0; j<statesList.size(); j++) {
            for(int i=0; i<defaultStaticData.size();i++) {
                DataCompt dc = new DataCompt();
                dc.setCompt(newCompt);
                dc.setState(statesList.get(j));
                dc.setStaticData(defaultStaticData.get(i));
                dc.setChecked(defaultIndeces[j]==i?1:0);
                em.merge(dc);
                LOGGER.info("new dc: "+dc);
            }
        }

    }
}

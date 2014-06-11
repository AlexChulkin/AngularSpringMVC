package com.luxoft.snp.dao;

import com.luxoft.snp.domain.Compt;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.List;
import javax.persistence.Query;
import com.luxoft.snp.domain.*;


import com.luxoft.snp.domain.State;


import org.springframework.transaction.annotation.Transactional;

@Repository
public class ComptDao {
    private static String[] removeQueryArray ={
            "REMOVE  FROM DATA_COMPT;",
            "REMOVE  FROM COMPT;",
            "REMOVE  FROM PACKET;",
            "REMOVE  FROM STATE;",
            "REMOVE  FROM STATIC_DATA;"
    };

    private static String[] queryArray ={

            "INSERT INTO STATE (LABEL) VALUES (\'PRE_COMMITEE\');",
            "INSERT INTO STATE (LABEL) VALUES (\'IN_COMMITEE\');",
            "INSERT INTO STATE (LABEL) VALUES (\'FINAL\');",

            "insert into PACKET (STATE_ID_FK) VALUES  (\'1\');",

            "insert into STATIC_DATA (LABEL) values (\'VERY_WEAK\');",
            "insert into STATIC_DATA (LABEL) values (\'WEAK\');",
            "insert into STATIC_DATA (LABEL) values (\'MODERATE\');", 
            "insert into STATIC_DATA (LABEL) values (\'ADEQUATE\');", 
            "insert into STATIC_DATA (LABEL) values (\'STRONG\');", 
            "insert into STATIC_DATA (LABEL) values (\'VERY_STRONG\');",

            "insert into COMPT (LABEL, PACKET_ID_FK) values (\'Risk position with Extraordinary Support\', \'1\');", 
            "insert into COMPT (LABEL, PACKET_ID_FK) values (\'Growth and Changes in Exposure\', \'1\');", 
            "insert into COMPT (LABEL, PACKET_ID_FK) values (\'Risk Concentrations and Risk Diversification\', \'1\');", 
            "insert into COMPT (LABEL, PACKET_ID_FK) values (\'Complexity\', \'1\');", 
            "insert into COMPT (LABEL, PACKET_ID_FK) values (\'Risks not covered by RACF\', \'1\');", 
            "insert into COMPT (LABEL, PACKET_ID_FK) values (\'Evidence of stronger or weaker Loss Experience\', \'1\');",

            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'1\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'1\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'1\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'1\', \'4\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'1\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'1\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'2\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'2\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'2\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'2\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'2\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'2\', \'6\', \'1\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'3\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'3\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'3\', \'3\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'3\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'3\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'1\', \'3\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'1\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'1\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'1\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'1\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'1\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'1\', \'6\', \'1\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'2\', \'1\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'2\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'2\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'2\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'2\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'2\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'3\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'3\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'3\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'3\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'3\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'2\', \'3\', \'6\', \'1\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'1\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'1\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'1\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'1\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'1\', \'5\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'1\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'2\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'2\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'2\', \'3\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'2\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'2\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'2\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'3\', \'1\', \'1\');",
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'3\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'3\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'3\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'3\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'3\', \'3\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'1\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'1\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'1\', \'3\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'1\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'1\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'1\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'2\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'2\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'2\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'2\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'2\', \'5\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'2\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'3\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'3\', \'2\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'3\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'3\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'3\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'4\', \'3\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'1\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'1\', \'2\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'1\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'1\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'1\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'1\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'2\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'2\', \'2\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'2\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'2\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'2\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'2\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'3\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'3\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'3\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'3\', \'4\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'3\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'5\', \'3\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'1\', \'1\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'1\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'1\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'1\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'1\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'1\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'2\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'2\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'2\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'2\', \'4\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'2\', \'5\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'2\', \'6\', \'0\');", 
             
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'3\', \'1\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'3\', \'2\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'3\', \'3\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'3\', \'4\', \'0\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'3\', \'5\', \'1\');", 
            "insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( \'6\', \'3\', \'6\', \'0\');"            
    };


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
        return em.createQuery("select distinct s from State s ", State.class).getResultList();

    }


    @Transactional
    public void insertData() {
        for(String query:queryArray) {
            em.createNativeQuery(query).executeUpdate();
        }
    }
    @Transactional
    public void removeData() {
        for(String query:removeQueryArray) {
            em.createNativeQuery(query).executeUpdate();
        }
    }


    @Transactional(readOnly=true)
    @SuppressWarnings("unchecked")
    public List<Compt> getComponents(int packetId){
        return em.createQuery("select compt from Compt compt where compt.packet.id=:packetId")
                .setParameter("packetId", packetId).getResultList();
    }
}

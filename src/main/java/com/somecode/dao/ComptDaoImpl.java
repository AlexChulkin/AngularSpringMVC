package com.somecode.dao;

import com.google.common.collect.Lists;
import com.somecode.domain.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Autowired
    private DataComptRepository dataComptRepository;

    @PostConstruct
    @Transactional(readOnly = true)
    private void setDefaultStaticData(){
        defaultStaticData = Lists.newArrayList(staticDataRepository.findAllByOrderByIdAsc());
    }

    @Override
    public List<Object[]> getStaticData(long packetId){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<StaticData> sd = cq.from(StaticData.class);
        Root<Compt> c = cq.from(Compt.class);
        Root<DataCompt> dc = cq.from(DataCompt.class);
        cq.multiselect(dc.get("id"),c.get("id"), dc.get("state").get("id"), sd.get("label"), dc.get("checked"));
        cq.orderBy(cb.asc(dc.get("id")));

        Predicate criteria = cb.conjunction();
        Predicate p = cb.equal(c.get("packet").get("id"),packetId);
        criteria = cb.and(criteria, p);
        p = cb.equal(dc.get("compt").get("id"),c.get("id"));
        criteria = cb.and(criteria, p);
        p = cb.equal(dc.get("staticData").get("id"),sd.get("id"));
        criteria = cb.and(criteria, p);

        cq.where(criteria);
        return em.createQuery(cq).getResultList();
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
    private List<DataCompt> getDataCompts(long comptId){
        return dataComptRepository.findByCompt_id(comptId);
    }

    private List<Integer> getDefaultIndeces(String[] defaultVals) {
        LOGGER.info("The default Values: " + Arrays.toString(defaultVals));
        List<String> defaultValsList = Arrays.asList(defaultVals);
        List<String> defaultStaticDataLabels = new ArrayList<>();
        defaultStaticData.forEach(e -> defaultStaticDataLabels.add(e.getLabel()));

        List<Integer> defaultIndeces = new ArrayList<>(defaultVals.length);
        defaultValsList.forEach(e -> defaultIndeces.add(defaultStaticDataLabels.indexOf(e)));
        LOGGER.info("The default Indeces found: " + defaultIndeces);
        return defaultIndeces;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public void updateCompt(long comptId, String[] defaultVals) {
        List<Integer>  defaultIndeces = getDefaultIndeces(defaultVals);
        List<DataCompt> dataComptsList = getDataCompts(comptId);
        for(DataCompt dc : dataComptsList){
            int defaultStateIndex = (int) dc.getState().getId()-1;
            int staticDataIndex = (int) dc.getStaticData().getId()-1;
            boolean checked = dc.getChecked();
            if(!checked && defaultIndeces.get(defaultStateIndex)==staticDataIndex
                    || checked && defaultIndeces.get(defaultStateIndex)!=staticDataIndex){
                dc.setChecked(!checked);
                em.merge(dc);
                LOGGER.info("Updated data compt with id = "+dc.getId());
            }
        }
    }

    @Override
    @Transactional
    public void removeCompts(Long[] idsToRemove) {
        LOGGER.info("Ids To remove: "+Arrays.toString(idsToRemove));
        List<Long> idsToRemoveList = Arrays.asList(idsToRemove);
        List<DataCompt> removedDataCompts = dataComptRepository.removeByCompt_IdIn(idsToRemoveList);
        List<Compt> removedCompts = comptRepository.removeByIdIn(idsToRemoveList);

        LOGGER.info("Data Components removed: "+removedDataCompts);
        LOGGER.info("Components removed: "+removedCompts);
    }

    @Override
    @Transactional
    public void addCompt(String label, long packetId, String[] defaultVals) {
        LOGGER.info("entered the addCompt");
        List<Integer> defaultIndeces = getDefaultIndeces(defaultVals);
        Compt newCompt = new Compt();
        newCompt.setLabel(label);
        newCompt.setPacket(getPacket(packetId));
        newCompt = comptRepository.save(newCompt);
        LOGGER.info("new Compt: "+newCompt);

        List<State> statesList = getStates();

        for(int j=0; j<statesList.size(); j++) {
            for(int i=0; i<defaultStaticData.size();i++) {
                DataCompt dc = new DataCompt();
                dc.setCompt(newCompt);
                dc.setState(statesList.get(j));
                dc.setStaticData(defaultStaticData.get(i));
                dc.setChecked(defaultIndeces.get(j)==i);
                dataComptRepository.save(dc);
                LOGGER.info("New dataCompt: "+dc);
            }
        }

    }
}

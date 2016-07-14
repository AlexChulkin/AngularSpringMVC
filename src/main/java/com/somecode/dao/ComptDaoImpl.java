package com.somecode.dao;

import com.google.common.collect.Lists;
import com.somecode.domain.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
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
import java.util.Set;

@Repository
@Transactional
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
    public List<Object[]> getComponents(long packetId){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Compt> c = cq.from(Compt.class);
        cq.multiselect(c.get("id"), c.get("label"));
        cq.orderBy(cb.asc(c.get("id")));

        Predicate p = cb.equal(c.get("packet").get("id"),packetId);

        cq.where(p);

        return em.createQuery(cq).getResultList();
    }

    @Transactional(readOnly=true)
    private Set<DataCompt> getDataCompts(long comptId){
        Compt compt = comptRepository.findOne(comptId);
        return compt.getDataCompts();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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
    public void updateCompt(long comptId, String[] defaultVals) {
        List<Integer>  defaultIndeces = getDefaultIndeces(defaultVals);
        Set<DataCompt> dataCompts = getDataCompts(comptId);
        for(DataCompt dc : dataCompts){
            int defaultStateIndex = (int) dc.getState().getId()-1;
            int staticDataIndex = (int) dc.getStaticData().getId()-1;
            boolean checked = dc.getChecked();
            if(!checked && defaultIndeces.get(defaultStateIndex)==staticDataIndex
                    || checked && defaultIndeces.get(defaultStateIndex)!=staticDataIndex){
                dc.setChecked(!checked);
                em.merge(dc);
                LOGGER.info("Updated data compt with id = " + dc.getId());
            }
        }
    }

    @Override
    public void removeCompts(Long[] idsToRemove) {
        String idsToRemoveString = Arrays.toString(idsToRemove);
        LOGGER.info("The component ids To remove: " + idsToRemoveString);
        List<Long> idsToRemoveList = Arrays.asList(idsToRemove);

        comptRepository.removeByIdIn(idsToRemoveList);

        LOGGER.info("Ids of the removed components: " + idsToRemoveString);
    }

    @Override
    public void addCompt(String label, long packetId, String[] defaultVals) {
        List<Integer> defaultIndeces = getDefaultIndeces(defaultVals);
        Compt newCompt = new Compt();
        newCompt.setLabel(label);
        newCompt.setPacket(getPacket(packetId));

        List<State> statesList = getStates();

        for(int j=0; j<statesList.size(); j++) {
            for(int i=0; i<defaultStaticData.size();i++) {
                DataCompt dc = new DataCompt();
                dc.setState(statesList.get(j));
                dc.setStaticData(defaultStaticData.get(i));
                dc.setChecked(defaultIndeces.get(j)==i);
                newCompt.addDataCompt(dc);
            }
        }

        newCompt = comptRepository.save(newCompt);
        LOGGER.info("New Compt added: " + newCompt);
    }
}

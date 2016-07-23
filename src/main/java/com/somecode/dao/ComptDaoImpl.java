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
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Transactional
public class ComptDaoImpl implements  ComptDao {
    private static final Logger LOGGER = Logger.getLogger(ComptDaoImpl.class);

    private List<ComboData> defaultComboData;

//    private CriteriaBuilder cb;
//    CriteriaDelete<DataCompt> dataComptCriteriaDelete;
//    Path<DataCompt> dataComptPath;
//    CriteriaDelete<Compt> comptCriteriaDelete;
//    Path<Compt> comptPath;
//
//    private static String COMPT = "compt";
//    private static String ID = "id";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ComboDataRepository comboDataRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private ComptRepository comptRepository;

    @PostConstruct
    @Transactional(readOnly = true)
    private void setdefaultData() {
        defaultComboData = Lists.newArrayList(comboDataRepository.findAllByOrderByIdAsc());
//        cb = em.getCriteriaBuilder();
//        dataComptCriteriaDelete = cb.createCriteriaDelete(DataCompt.class);
//        dataComptPath = dataComptCriteriaDelete.from(DataCompt.class).get(COMPT).get(ID);
//        comptCriteriaDelete = cb.createCriteriaDelete(Compt.class);
//        comptPath = comptCriteriaDelete.from(Compt.class).get(ID);
    }

    @Override
    @Transactional(readOnly=true)
    public List<ComptSupplInfo> getComptsSupplInfo(long packetId) {

        return em.createNamedQuery("Compt.getSupplInfo",ComptSupplInfo.class)
                .setParameter("packetId", packetId)
                .getResultList();
    }

    @Override
    @Transactional(readOnly=true)
    public Packet getPacket(long packetId) {
        return em.find(Packet.class, packetId);
    }

    @Override
    @Transactional(readOnly=true)
    public List<State> getStates(){
        return Lists.newArrayList(stateRepository.findAll());
    }


    @Override
    @Transactional(readOnly=true)
    public List<ComboData> getDefaultComboData() {
        return defaultComboData;
    }

    @Override
    @Transactional(readOnly=true)
    public List<ComptInfo> getCompts(long packetId){
        return em.createNamedQuery("Compt.getInfo",ComptInfo.class)
                .setParameter("packetId", packetId)
                .getResultList();
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private List<Integer> getDefaultIndeces(List<String> defaultVals) {
        LOGGER.info("The default Values: " + defaultVals);
        List<String> defaultComboDataLabels = new ArrayList<>();
        defaultComboData.forEach(e -> defaultComboDataLabels.add(e.getLabel()));

        List<Integer> defaultIndeces = new ArrayList<>(defaultVals.size());
        defaultVals.forEach(e -> defaultIndeces.add(defaultComboDataLabels.indexOf(e)));
        LOGGER.info("The default Indeces found: " + defaultIndeces);
        return defaultIndeces;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateCompts(List<ComptsParams> comptsParamsList) {
        for (ComptsParams comptsParams : comptsParamsList) {
            List<Integer> defaultIndeces = getDefaultIndeces(comptsParams.getDefaultVals());
            Compt compt = em.find(Compt.class, comptsParams.getId());
            em.lock(compt, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            Set<DataCompt> dataCompts = compt.getDataCompts();

            for (DataCompt dc : dataCompts) {
                int defaultStateIndex = (int) dc.getState().getId() - 1;
                int comboDataIndex = (int) dc.getComboData().getId() - 1;
                boolean checked = dc.getChecked();
                if (!checked && defaultIndeces.get(defaultStateIndex) == comboDataIndex
                        || checked && defaultIndeces.get(defaultStateIndex) != comboDataIndex) {
                    dc.setChecked(!checked);
                }
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePacketsState(long packetId, long newStateId) {
        Packet packet = em.find(Packet.class, packetId);
        em.lock(packet, LockModeType.OPTIMISTIC);
        long oldStateId = packet.getState().getId();
        if (oldStateId != newStateId) {
            State newState = em.find(State.class, newStateId);
            packet.setState(newState);
//            em.merge(packet);
            LOGGER.info("The packet's " + packet.getId() + "state updated.");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeCompts(List<String> idsToRemove) {
        List<Long> longIdsToRemove = idsToRemove.stream()
                .mapToLong(Long::parseLong).boxed().collect(Collectors.toList());

        Compt firstCompt = em.find(Compt.class, longIdsToRemove.get(0));
//        System.out.println("firtcompt "+firstCompt);
        Packet packet = firstCompt.getPacket();
//        System.out.println("packet "+packet);
//        em.lock(packet, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        List<Compt> compts = comptRepository.findByIdIn(longIdsToRemove);
//        List<DataCompt> dataCompts = dataComptRepository.findByCompt_IdIn(longIdsToRemove);
//        System.out.println("comptscompts "+compts);
//        compts.forEach(c -> em.lock(c, LockModeType.OPTIMISTIC_FORCE_INCREMENT));
//        dataComptRepository.delete(dataCompts);

        comptRepository.delete(compts);
//        dataComptCriteriaDelete.where(dataComptPath.in(idsToRemove));
//        Query dataComptsDeleteQuery = em.createQuery(dataComptCriteriaDelete);
//        dataComptsDeleteQuery.setHint("javax.persistence.query.timeout", 5000);
//        try {
//            dataComptsDeleteQuery.executeUpdate();
//        } catch (QueryTimeoutException|PersistenceException e) {
//            LOGGER.info("Data Compts delete query lasted too long, restarting...");
//            dataComptsDeleteQuery.executeUpdate();
//        }
//
//
//        comptCriteriaDelete.where(comptPath.in(idsToRemove));
//        Query comptsDeleteQuery = em.createQuery(comptCriteriaDelete);
//        comptsDeleteQuery.setHint("javax.persistence.query.timeout", 5000);
//        try {
//            comptsDeleteQuery.executeUpdate();
//        } catch (QueryTimeoutException|PersistenceException e) {
//            LOGGER.info("Compts delete query lasted too long, restarting...");
//            comptsDeleteQuery.executeUpdate();
//        }

        LOGGER.info("Ids of the removed components: " + idsToRemove);
    }

    @Override
    public void addCompts(long packetId, List<ComptsParams> comptsParamsList) {
        Packet packet = getPacket(packetId);
        List<State> statesList = getStates();
        List<Compt> comptList = new ArrayList<>(comptsParamsList.size());
        List<Long> comptIdsList = new ArrayList<>(comptsParamsList.size());

//        em.lock(packet, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        for (ComptsParams ComptsParams : comptsParamsList) {
            List<Integer> defaultIndeces = getDefaultIndeces(ComptsParams.getDefaultVals());
            Compt newCompt = new Compt();
            newCompt.setLabel(ComptsParams.getLabel());
            packet.addCompt(newCompt);

            for (int j = 0; j < statesList.size(); j++) {
                for (int i = 0; i < defaultComboData.size(); i++) {
                    DataCompt dc = new DataCompt();
                    dc.setState(statesList.get(j));
                    dc.setComboData(defaultComboData.get(i));
                    dc.setChecked(defaultIndeces.get(j) == i);
                    newCompt.addDataCompt(dc);
                }
            }
            comptList.add(newCompt);
        }
        comptRepository.save(comptList).forEach(compt -> comptIdsList.add(compt.getId()));
        LOGGER.info("Persisted compt ids: " + comptIdsList);
    }
}

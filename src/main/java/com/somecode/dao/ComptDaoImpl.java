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
@Transactional(readOnly = true)
public class ComptDaoImpl implements  ComptDao {
    private static final Logger LOGGER = Logger.getLogger(ComptDaoImpl.class);

    private List<ComboData> defaultComboData;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ComboDataRepository comboDataRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private ComptRepository comptRepository;

    @PostConstruct
    private void setdefaultData() {
        defaultComboData = Lists.newArrayList(comboDataRepository.findAllByOrderByIdAsc());
    }

    @Override
    public List<ComptSupplInfo> getComptsSupplInfo(long packetId) {

        return em.createNamedQuery("Compt.getSupplInfo",ComptSupplInfo.class)
                .setParameter("packetId", packetId)
                .getResultList();
    }

    @Override
    public Packet getPacket(long packetId) {
        return em.find(Packet.class, packetId);
    }

    @Override
    public List<State> getStates(){
        return Lists.newArrayList(stateRepository.findAll());
    }


    @Override
    public List<ComboData> getDefaultComboData() {
        return defaultComboData;
    }

    @Override
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
            LOGGER.info("The packet's " + packet.getId() + "state updated.");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeCompts(List<String> idsToRemove) {
        List<Long> longIdsToRemove = idsToRemove.stream()
                .mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        List<Compt> compts = comptRepository.findByIdIn(longIdsToRemove);

        comptRepository.delete(compts);
        LOGGER.info("Ids of the removed components: " + idsToRemove);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addCompts(long packetId, List<ComptsParams> comptsParamsList) {
        Packet packet = getPacket(packetId);
        List<State> statesList = getStates();
        List<Compt> comptList = new ArrayList<>(comptsParamsList.size());
        List<Long> comptIdsList = new ArrayList<>(comptsParamsList.size());

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

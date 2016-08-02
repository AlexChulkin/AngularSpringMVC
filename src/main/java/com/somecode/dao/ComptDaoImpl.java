package com.somecode.dao;

import com.google.common.collect.Lists;
import com.somecode.domain.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Repository
@Transactional(readOnly = true)
public class ComptDaoImpl implements  ComptDao {
    private static final Logger LOGGER = Logger.getLogger(ComptDaoImpl.class);

    private List<ComboData> defaultComboData;

    private Map<String, Integer> mapComboLabelsToIndices;

    private List<State> states;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ComboDataRepository comboDataRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private ComptRepository comptRepository;

    @Override
    public List<ComptSupplInfo> getComptsSupplInfo(long packetId) {
        return em
                .createNamedQuery("Compt.getSupplInfo", ComptSupplInfo.class)
                .setParameter("packetId", packetId)
                .getResultList();
    }

    @Override
    public Long getPacketStateId(long packetId) {
        Packet packet = em.find(Packet.class, packetId);
        if (packet == null) {
            return null;
        }
        Optional<Long> longOptional = Optional.of(packet)
                .map(Packet::getState)
                .map(State::getId);
        return longOptional.isPresent() ? longOptional.get() : null;
    }

    private Packet getPacket(long packetId) {
        return em.find(Packet.class, packetId);
    }

    @Override
    public List<State> getStates(){
        states = Lists.newArrayList(stateRepository.findAll());
        System.out.println("states " + states);

        return states;
    }

    @Override
    public List<ComboData> getDefaultComboData() {
        List<ComboData> oldDefaultComboData = defaultComboData;
        defaultComboData = Lists.newArrayList(comboDataRepository.findAllByOrderByIdAsc());
        System.out.println("defaultComboData " + defaultComboData);

        if (defaultComboData.equals(oldDefaultComboData)) {
            return defaultComboData;
        }
        mapComboLabelsToIndices = IntStream
                .range(0, defaultComboData.size())
                .boxed()
                .collect(Collectors.toMap(i -> defaultComboData.get(i).getLabel(), Function.identity()));

        return defaultComboData;
    }

    @Override
    public List<ComptInfo> getCompts(long packetId){
        List<ComptInfo> getCompts = em.createNamedQuery("Compt.getInfo", ComptInfo.class)
                .setParameter("packetId", packetId)
                .getResultList();
        System.out.println("getCompts " + getCompts);
        return getCompts;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private List<Integer> getIndices(List<String> vals) {
        getDefaultComboData();
        return vals.stream().map(mapComboLabelsToIndices::get).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> updateCompts(List<ComptsParams> comptsParamsList) {
        List<Long> result = new ArrayList<>();

        for (ComptsParams comptsParams : comptsParamsList) {
            Compt compt = em.find(Compt.class, comptsParams.getId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            if (compt == null) {
                LOGGER.info("Compt update. The compt with id " + comptsParams.getId() + "does not exist.");
                continue;
            }

            List<Integer> newCheckedIndices = getIndices(comptsParams.getVals());
            List<DataCompt> dataCompts = compt.getDataCompts();

            for (DataCompt dc : dataCompts) {
                int defaultStateIndex = (int) dc.getState().getId() - 1;
                int comboDataIndex = (int) dc.getComboData().getId() - 1;
                boolean checked = dc.getChecked();
                if (!checked && newCheckedIndices.get(defaultStateIndex) == comboDataIndex
                        || checked && newCheckedIndices.get(defaultStateIndex) != comboDataIndex) {
                    dc.setChecked(!checked);
                }
            }
            result.add(compt.getId());
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long updatePacketState(long packetId, long newStateId) {
        Packet packet = em.find(Packet.class, packetId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        if (packet == null) {
            LOGGER.info("Packet state update. The packet with id " + packetId + "does not exist.");
            return null;
        }
        long oldStateId = packet.getState().getId();
        if (oldStateId != newStateId) {
            State newState = em.find(State.class, newStateId);
            if (newState == null) {
                LOGGER.info("Packet state update. The state with id " + newStateId + "does not exist.");
                return null;
            }
            packet.setState(newState);
            em.persist(packet);
            LOGGER.info("The packet's " + packet.getId() + "state updated.");
            return packet.getId();
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> removeCompts(List<Long> idsToRemove) {
        List<Compt> compts = comptRepository
                .findByIdIn(idsToRemove)
                .stream()
                .filter(c -> c != null)
                .collect(Collectors.toList());

        comptRepository.delete(compts);
        List<Long> result = compts.stream().mapToLong(Compt::getId).boxed().collect(Collectors.toList());
        LOGGER.info("Ids of the removed components: " + result);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> addCompts(long packetId, List<ComptsParams> comptsParamsList) {
        Packet packet = getPacket(packetId);
        if (packet == null) {
            LOGGER.info("Packet state add compts. The packet with id " + packetId + "does not exist.");
            return Collections.EMPTY_LIST;
        }

        List<State> statesList = getStates();
        List<Compt> comptList = new ArrayList<>(comptsParamsList.size());
        List<Long> comptIdsList = new ArrayList<>(comptsParamsList.size());

        for (ComptsParams comptsParams : comptsParamsList) {
            List<Integer> defaultComboDataIndeces = getIndices(comptsParams.getVals());
            Compt newCompt = new Compt();
            newCompt.setLabel(comptsParams.getLabel());
            packet.addCompt(newCompt);

            int numOfStates = states.size();
            int numOfComboDataItems = defaultComboData.size();

            for (int j = 0; j < numOfStates; j++) {
                for (int i = 0; i < numOfComboDataItems; i++) {
                    DataCompt dc = new DataCompt();
                    dc.setState(statesList.get(j));
                    dc.setComboData(defaultComboData.get(i));
                    dc.setChecked(defaultComboDataIndeces.get(j) == i);
                    newCompt.addDataCompt(dc);
                }
            }
            comptList.add(newCompt);
        }

        comptRepository.save(comptList).forEach(compt -> comptIdsList.add(compt.getId()));
        LOGGER.info("Persisted compt ids: " + comptIdsList);
        return comptIdsList;
    }
}

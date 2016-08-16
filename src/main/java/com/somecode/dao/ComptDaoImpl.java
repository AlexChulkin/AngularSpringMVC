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

    private List<ComboData> allComboData = Collections.EMPTY_LIST;

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

    @Autowired
    private PacketRepository packetRepository;

    @Override
    public List<ComptSupplInfo> getComptsSupplInfoByPacketId(long packetId) {
        return em
                .createNamedQuery("Compt.getComptsSupplInfoByPacketId", ComptSupplInfo.class)
                .setParameter("packetId", packetId)
                .getResultList();
    }

    @Override
    public List<PacketInfo> getAllPackets() {
        List<PacketInfo> list = em
                .createNamedQuery("Packet.getAllPackets", PacketInfo.class)
                .getResultList();
        return list;
    }

    @Override
    public List<ComptSupplInfo> getAllComptsSupplInfo() {
        return em
                .createNamedQuery("Compt.getAllComptsSupplInfo", ComptSupplInfo.class)
                .getResultList();
    }

    @Override
    public Long getPacketStateId(long packetId) {
        Packet packet = getPacket(packetId);
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
    public List<State> getAllStates() {
        Iterable<State> iterable = stateRepository.findAll();
        states = Lists.newArrayList(iterable);
        System.out.println("states " + states);

        return states;
    }

    @Override
    public List<ComboData> getAllComboData() {
        List<ComboData> oldAllComboData = allComboData;
        allComboData = Lists.newArrayList(comboDataRepository.findAll());
        LOGGER.info("All Combo Data " + allComboData);

        if (checkComboDataListsForEquality(allComboData, oldAllComboData)) {
            return allComboData;
        }
        mapComboLabelsToIndices = IntStream
                .range(0, allComboData.size())
                .boxed()
                .collect(Collectors.toMap(i -> allComboData.get(i).getLabel(), Function.identity()));

        return allComboData;
    }

    private boolean checkComboDataListsForEquality(List<ComboData> allComboData,
                                                   List<ComboData> oldAllComboData) {
        int size = allComboData.size();
        if (size != oldAllComboData.size()) {
            return false;
        }
        return IntStream.range(0, size).boxed()
                .allMatch(i ->
                        getSortedLabels(allComboData).get(i).equals(getSortedLabels(oldAllComboData).get(i)));
    }

    private List<String> getSortedLabels(List<ComboData> comboDataList) {
        return comboDataList.stream().map(ComboData::getLabel).sorted().collect(Collectors.toList());
    }

    @Override
    public List<ComptInfo> getComptsByPacketId(long packetId) {
        List<ComptInfo> getCompts = em.createNamedQuery("Compt.getComptsByPacketId", ComptInfo.class)
                .setParameter("packetId", packetId)
                .getResultList();
        return getCompts;
    }

    @Override
    public List<ComptInfo> getAllCompts() {
        List<ComptInfo> getCompts = em.createNamedQuery("Compt.getAllCompts", ComptInfo.class)
                .getResultList();
        return getCompts;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private List<Integer> getIndicesFromVals(List<String> vals) {
        getAllComboData();
        return vals.stream().map(mapComboLabelsToIndices::get).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> updateCompts(List<ComptParams> comptParamsList) {
        List<Long> result = new ArrayList<>();

        for (ComptParams comptParams : comptParamsList) {
            long comptId = comptParams.getId();
            Compt compt = em.find(Compt.class, comptId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            if (compt == null) {
                LOGGER.error("Compt update. The compt with id " + comptId + "does not exist.");
                continue;
            }
            List<Integer> newCheckedIndices = getIndicesFromVals(comptParams.getVals());
            List<DataCompt> dataCompts = compt.getDataCompts();
            for (DataCompt dc : dataCompts) {
                int stateIndex = (int) dc.getState().getId() - 1;
                int comboDataIndex = (int) dc.getComboData().getId() - 1;
                boolean checked = dc.getChecked();
                if (!checked && newCheckedIndices.get(stateIndex) == comboDataIndex
                        || checked && newCheckedIndices.get(stateIndex) != comboDataIndex) {
                    dc.setChecked(!checked);
                    LOGGER.info("DataCompt updated widh id = " + dc.getId());
                }
            }
            result.add(compt.getId());
        }
        LOGGER.info("List of the updated compts: " + result);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> updatePackets(List<PacketParams> updatedPackets) {
        List<Long> result = new ArrayList<>();
        for (PacketParams p : updatedPackets) {
            long packetId = p.getId();
            Packet packet = em.find(Packet.class, packetId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            if (packet == null) {
                LOGGER.error("Packet state update. The packet with id " + packetId + "does not exist.");
                continue;
            }
            long newStateId = p.getStateId();
            State newState = em.find(State.class, newStateId);
            if (newState == null) {
                LOGGER.error("Packet state update. The state with id " + newStateId + "does not exist.");
                continue;
            }
            packet.setState(newState);
            result.add(packetId);
        }
        LOGGER.info("List of the updated packet ids: " + result);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> deleteCompts(List<Long> idsToDelete) {
        List<Compt> compts = comptRepository
                .findByIdIn(idsToDelete)
                .stream()
                .filter(c -> c != null)
                .collect(Collectors.toList());

        comptRepository.delete(compts);
        List<Long> result = getIdsFromEntities(compts.toArray(new Compt[0]));
        LOGGER.info("Deleted components with the following ids: " + result);
        return result;
    }

    private List<Long> getIdsFromEntities(EntityType[] entities) {
        return Arrays.stream(entities).mapToLong(EntityType::getId).boxed().collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> deletePackets(List<Long> idsToDelete) {
        List<Packet> packets = packetRepository
                .findByIdIn(idsToDelete)
                .stream()
                .filter(p -> p != null)
                .collect(Collectors.toList());
        if (packets.size() == 0) {
            LOGGER.error("Can't delete any of the packets with the following ids cause " +
                    "they don't exist: " + idsToDelete);
            return Collections.EMPTY_LIST;
        }
        packetRepository.delete(packets);
        List<Long> result = getIdsFromEntities(packets.toArray(new Packet[0]));
        LOGGER.info("Deleted packets with the following ids: " + result);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> addCompts(long packetId, List<ComptParams> comptParamsList) {
        Packet packet = getPacket(packetId);
        if (packet == null) {
            LOGGER.info("Packet state add compts. The packet with id " + packetId + "does not exist.");
            return Collections.EMPTY_LIST;
        }

        List<Long> comptIdsList = new ArrayList<>(comptParamsList.size());
        List<Compt> comptList = preparePacketAndComptsForSaving(packet, comptParamsList);
        comptRepository.save(comptList).forEach(compt -> comptIdsList.add(compt.getId()));
        LOGGER.info("Persisted new compt with ids: " + comptIdsList + " for packet#" + packetId);
        return comptIdsList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> addPackets(List<PacketParams> packetParamsList) {
        List<Long> result = new ArrayList<>();
        List<Packet> packets = new ArrayList<>(packetParamsList.size());
        for (PacketParams packetParams : packetParamsList) {
            State state = em.find(State.class, packetParams.getStateId());
            Packet packet = new Packet(packetParams.getId(), state);
            preparePacketAndComptsForSaving(packet, packetParams.getComptParamsList());
            packets.add(packet);
        }
        packetRepository.save(packets).forEach(pkt -> {
            LOGGER.info("Persisted new packet with id: " + pkt.getId() + " and compts with ids: " + pkt.getComptIds());
        });
        return result;
    }

    private List<Compt> preparePacketAndComptsForSaving(Packet packet, List<ComptParams> comptParamsList) {
        List<Compt> result = new ArrayList<>(comptParamsList.size());
        List<State> statesList = getAllStates();

        for (ComptParams comptParams : comptParamsList) {
            List<Integer> allComboDataIndeces = getIndicesFromVals(comptParams.getVals());
            Compt newCompt = new Compt();
            newCompt.setLabel(comptParams.getLabel());
            packet.addCompt(newCompt);

            int numOfStates = states.size();
            int numOfComboDataItems = allComboData.size();

            for (int j = 0; j < numOfStates; j++) {
                for (int i = 0; i < numOfComboDataItems; i++) {
                    DataCompt dc = new DataCompt();
                    dc.setState(statesList.get(j));
                    dc.setComboData(allComboData.get(i));
                    dc.setChecked(allComboDataIndeces.get(j) == i);
                    newCompt.addDataCompt(dc);
                }
            }
            result.add(newCompt);
        }
        return result;
    }

}

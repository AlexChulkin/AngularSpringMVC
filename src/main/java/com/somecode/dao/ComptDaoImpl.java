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
    private static final Integer DEFAULT_COMBO_DATA_INDEX = 0;
    private static final Long DEFAULT_STATE_INDEX = 0L;


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
        return em.createNamedQuery("Compt.getAllComptsSupplInfo", ComptSupplInfo.class)
                .getResultList();
    }

    private Packet getPacket(long packetId) {
        return em.find(Packet.class, packetId);
    }

    @Override
    public List<State> getAllStates() {
        Iterable<State> iterable = stateRepository.findAll();
        states = Lists.newArrayList(iterable);
        LOGGER.info(" All states " + states);
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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

    @Transactional
    private List<Integer> getIndicesFromVals(List<String> vals, Long comptId, Long packetId)
            throws EmptyComboDataTableException {
        getAllComboData();
        if (allComboData.isEmpty()) {
            throw new EmptyComboDataTableException();
        }
        return vals.stream().map(v -> mapLabelToIndex(v, comptId, packetId)).collect(Collectors.toList());
    }

    private Integer mapLabelToIndex(String label, Long comptId, Long packetId) {
        Integer result = mapComboLabelsToIndices.get(label);
        if (result == null) {
            result = DEFAULT_COMBO_DATA_INDEX;
            String errorReport = generateNonExistingComboDataLabelErrorReport(label, comptId, packetId);
            LOGGER.error(errorReport);
        }
        return result;
    }

    private String generateNonExistingComboDataLabelErrorReport(String label, Long comptId, Long packetId) {
        String errorReport = "Label '" + label + "' doesn't exist. So it's " +
                "automatically replaced with the following one: '" +
                allComboData.get(DEFAULT_COMBO_DATA_INDEX).getLabel() + "'";
        StringBuilder sb = new StringBuilder();
        if (comptId != null) {
            errorReport = sb.append("Compt#" + comptId + " update. ")
                    .append(errorReport).toString();
        } else {
            errorReport = sb.append("Adding new compts to the packet#" + packetId + ". ")
                    .append(errorReport).toString();
        }
        return errorReport;
    }

    @Override
    @Transactional
    public Map<Long, List<Long>> updateCompts(List<ComptParams> comptParamsList) throws DatabaseException {
        Map<Long, List<Long>> result = new HashMap<>();

        for (ComptParams comptParams : comptParamsList) {
            long comptId = comptParams.getId();
            Compt compt = em.find(Compt.class, comptId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            if (compt == null) {
                LOGGER.error("Compt#" + comptId + " update. The compt with this id does not exist.");
                continue;
            }
            List<Integer> newCheckedIndices;
            try {
                newCheckedIndices = getIndicesFromVals(comptParams.getVals(), comptId, null);
            } catch (EmptyDBTableException cause) {
                LOGGER.error("Exception: " + cause.getMessage() + "\nStacktrace: " + cause.getStackTrace());
                DatabaseException exc = new DatabaseException();
                exc.initCause(cause);
                throw exc;
            }
            List<DataCompt> dataCompts = compt.getDataCompts();
            for (DataCompt dc : dataCompts) {
                int stateIndex = (int) dc.getState().getId() - 1;
                int comboDataIndex = (int) dc.getComboData().getId() - 1;
                boolean checked = dc.getChecked();
                if (!checked && newCheckedIndices.get(stateIndex) == comboDataIndex
                        || checked && newCheckedIndices.get(stateIndex) != comboDataIndex) {
                    dc.setChecked(!checked);
                    LOGGER.info("Compt#" + comptId + " update: DataCompt updated widh id = " + dc.getId());
                }
            }
            Long packetId = compt.getPacket().getId();
            if (result.get(packetId) == null) {
                result.put(packetId, new LinkedList<Long>());
            }
            result.get(packetId).add(comptId);
        }
        result.forEach((k, v) -> LOGGER.info("Packet#" + k + ": List of the updated compts: " + v));

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

        int comptsSize = compts.size();
        if (comptsSize == 0) {
            LOGGER.error("Compts' delete. Can't delete any of the compts with the following(ALL GIVEN TO METHOD)" +
                    " ids cause they don't exist: " + idsToDelete);
            return Collections.EMPTY_LIST;
        }

        comptRepository.delete(compts);
        List<Long> result = getIdsFromEntities(compts.toArray(new Compt[0]));
        LOGGER.info("Deleted compts with the following ids: " + result);

        if (comptsSize != idsToDelete.size()) {
            Set<Long> idsToDeleteSet = new HashSet<>(idsToDelete);
            LOGGER.error("Packets' delete. Can't delete any of the packets with the following ids cause " +
                    "they don't exist: " + idsToDeleteSet.removeAll(result));
        }

        return result;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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

        int packetsSize = packets.size();
        if (packetsSize == 0) {
            LOGGER.error("Packets' delete. Can't delete any of the packets with the following(ALL GIVEN TO METHOD)" +
                    " ids cause they don't exist: " + idsToDelete);
            return Collections.EMPTY_LIST;
        }
        packetRepository.delete(packets);
        List<Long> result = getIdsFromEntities(packets.toArray(new Packet[0]));
        LOGGER.info("Packets' delete. Deleted packets with the following ids: " + result);

        if (packetsSize != idsToDelete.size()) {
            Set<Long> idsToDeleteSet = new HashSet<>(idsToDelete);
            LOGGER.error("Packets' delete. Can't delete any of the packets with the following ids cause " +
                    "they don't exist: " + idsToDeleteSet.removeAll(result));
        }

        return result;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdatePackets(List<PacketParams> packetParamsList, OperationType operationType)
            throws DatabaseException {

        List<Packet> packets = new LinkedList<>();
        for (PacketParams packetParams : packetParamsList) {
            long stateId = packetParams.getStateId();
            State state = em.find(State.class, stateId);
            if (state == null) {
                LOGGER.info("Packets updating or saving. Adding new packets." +
                        " The state with id " + stateId + "does not exist.");
                continue;
            }
            Packet packet = new Packet();
            packet.setState(state);
            try {
                preparePacketAndComptsForSaving(packet, packetParams.getAddedComptParamsList());
            } catch (EmptyDBTableException cause) {
                LOGGER.error("Exception: " + cause.getMessage() + "\nStacktrace: " + cause.getStackTrace());
                DatabaseException exc = new DatabaseException();
                exc.initCause(cause);
                throw exc;
            }
            packets.add(packet);
        }

        Set<Long> updatedPacketIds = new HashSet<>();
        for (PacketParams packetParams : packetParamsList) {
            long packetId = packetParams.getId();
            updatedPacketIds.add(packetId);
            Packet packet = getPacket(packetId);
            if (packet == null) {
                LOGGER.info("Packets updating or saving. Updating the packet. " +
                        "The packet with id " + packetId + " does not exist.");
                continue;
            }
            List<ComptParams> addedComptParamsList = packetParams.getAddedComptParamsList();
            if (!addedComptParamsList.isEmpty()) {
                LOGGER.info("Packets updating or saving. The following compts for the packet#" + packetId +
                        " were added: " + preparePacketAndComptsForSaving(packet, addedComptParamsList));
            }

            updateState(packet, packetParams.getStateId());


            updateCompts(packetParams.getUpdatedComptParamsList(), packetId);
            packets.add(packet);
        }
        packetRepository.save(packets).forEach(pkt -> {
            LOGGER.info("Packets updating or saving. Persisted new packet#" + pkt.getId() +
                    " with the following state id: " + pkt.getState().getId() + " and compts: " + pkt.getCompts());
        });
    }

    @Transactional
    private void updateState(Packet packet, Long stateId) {
        if (stateId != null) {
            State state = em.find(State.class, stateId);
            if (state != null) {
                packet.setState(state);
                LOGGER.info("Packets updating or saving. The state for the packet#" + packet.getId() +
                        " was updated to the new one with id: " + stateId);
            } else {
                LOGGER.info("Packets updating or saving. Packet state update. " +
                        "The state with id " + stateId + "does not exist.");
            }
        }
    }

    @Transactional
    private List<Compt> preparePacketAndComptsForSaving(Packet packet, List<ComptParams> comptParamsList)
            throws EmptyDBTableException {
        List<Compt> result = new ArrayList<>(comptParamsList.size());
        List<State> statesList = getAllStates();

        for (ComptParams comptParams : comptParamsList) {
            List<Integer> allComboDataIndeces;

            allComboDataIndeces = getIndicesFromVals(comptParams.getVals(), null, packet.getId());
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

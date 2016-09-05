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
    private static final int DEFAULT_COMBO_DATA_INDEX = 0;
    private static final int DEFAULT_STATE_INDEX = 0;


    private List<ComboData> allComboData = Collections.EMPTY_LIST;
    private List<State> allStates = Collections.EMPTY_LIST;

    private Map<String, Integer> mapComboLabelsToIndices;

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
    public List<PacketInfo> loadPackets(Long packetId) {
        List<PacketInfo> result = new LinkedList<>();
        if (packetId != null) {
            Packet onlyResult = packetRepository.findOne(packetId);
            if (onlyResult == null) {
                return result;
            }
            result.add(new PacketInfo(onlyResult));
        } else {
            packetRepository.findAll().forEach(p -> result.add(new PacketInfo(p)));
        }
        LOGGER.info("All Packets Loaded, here are their ids: "
                + result.stream().map(PacketInfo::getId).collect(Collectors.toList()));
        return result;
    }

    @Override
    public List<ComptSupplInfo> loadComptsSupplInfo(Long packetId) {
        List<ComptSupplInfo> result;
        if (packetId == null) {
            result = em.createNamedQuery("Compt.loadAllComptsSupplInfo", ComptSupplInfo.class)
                    .getResultList();
        } else {
            result = em.createNamedQuery("Compt.loadComptsSupplInfoByPacketId", ComptSupplInfo.class)
                    .setParameter("packetId", packetId)
                    .getResultList();
        }

        LOGGER.info("The ComptsSupplInfo Loaded: " + result);
        return result;
    }

    private Packet loadPacket(Long packetId) {
        return em.find(Packet.class, packetId);
    }

    @Override
    public List<State> loadAllStates() throws DatabaseException {
        try {
            return loadAllStatesLocally();
        } catch (EmptyDBTableException cause) {
            LOGGER.error(cause.getStackTrace());
            DatabaseException exc = new DatabaseException();
            exc.initCause(cause);
            throw exc;
        }
    }

    private List<State> loadAllStatesLocally() throws EmptyStateTableException {
        Iterable<State> iterable = stateRepository.findAll();
        allStates = Lists.newArrayList(iterable);
        if (allStates.isEmpty()) {
            throw new EmptyStateTableException();
        }
        LOGGER.info("All states loaded: " + allStates);
        return allStates;
    }

    @Override
    public List<ComboData> loadAllComboData() throws DatabaseException {
        try {
            return loadAllComboDataLocally();
        } catch (EmptyDBTableException cause) {
            LOGGER.error(cause.getStackTrace());
            DatabaseException exc = new DatabaseException();
            exc.initCause(cause);
            throw exc;
        }
    }

    private List<ComboData> loadAllComboDataLocally() throws EmptyComboDataTableException {
        List<ComboData> oldAllComboData = allComboData;
        allComboData = Lists.newArrayList(comboDataRepository.findAll());
        if (allComboData.isEmpty()) {
            throw new EmptyComboDataTableException();
        }
        LOGGER.info("All Combo Data loaded: " + allComboData);

        if (checkComboDataListsForEquality(allComboData, oldAllComboData)) {
            return allComboData;
        }
        mapComboLabelsToIndices = IntStream
                .range(0, allComboData.size())
                .boxed()
                .collect(Collectors.toMap(i -> allComboData.get(i).getLabel(), Function.identity()));

        LOGGER.info("MapComboLabelsToIndices: " + mapComboLabelsToIndices);

        return allComboData;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private boolean checkComboDataListsForEquality(List<ComboData> allComboData,
                                                   List<ComboData> oldAllComboData) {
        int size = allComboData.size();
        return (size == oldAllComboData.size()) && IntStream.range(0, size).boxed()
                .allMatch(i -> getSortedLabels(allComboData).get(i).equals(getSortedLabels(oldAllComboData).get(i)));
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private List<String> getSortedLabels(List<ComboData> comboDataList) {
        return comboDataList.stream().map(ComboData::getLabel).sorted().collect(Collectors.toList());
    }

    @Override
    public List<ComptInfo> loadCompts(Long packetId) {
        List<ComptInfo> result;
        if (packetId != null) {
            result = comptRepository.findByPacket_Id(packetId).stream().map(ComptInfo::new).collect(Collectors.toList());
        } else {
            List<ComptInfo> listOfCompts = new LinkedList<>();
            comptRepository.findAll().forEach(c -> listOfCompts.add(new ComptInfo(c)));
            result = listOfCompts;
        }
        LOGGER.info(packetId == null
                ? "ComptService. Loaded All Compts: " + result
                : "ComptService. Loaded compts for packet#" + packetId + ": " + result);
        return result;
    }

    @Transactional
    private List<Integer> getIndicesFromVals(List<String> vals, Long comptId, Long packetId) {
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
        } else if (packetId != null) {
            errorReport = sb.append("Adding new compts to the packet#" + packetId + ". ")
                    .append(errorReport).toString();
        } else {
            errorReport = sb.append("Adding new compts to the non-persisted packet. ")
                    .append(errorReport).toString();
        }
        return errorReport;
    }

    @Override
    @Transactional
    public Map<Long, List<Long>> updateCompts(List<ComptParams> comptParamsList) throws DatabaseException {
        loadAllComboData();
        Map<Long, List<Long>> result = new HashMap<>();

        for (ComptParams comptParams : comptParamsList) {
            long comptId = comptParams.getId();
            Compt compt = em.find(Compt.class, comptId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            if (compt == null) {
                LOGGER.error("Compt#" + comptId + " update. The compt with this id does not exist.");
                continue;
            }
            List<Integer> newCheckedIndices;
            newCheckedIndices = getIndicesFromVals(comptParams.getVals(), comptId, null);
            List<DataCompt> dataCompts = compt.getDataCompts();
            for (DataCompt dc : dataCompts) {
                int stateIndex = (int) (dc.getState().getId() - 1);
                int comboDataIndex = (int) (dc.getComboData().getId() - 1);
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
    public void addOrUpdatePackets(List<PacketParams> packetParamsList, OperationType operationType)
            throws DatabaseException {
        loadAllStates();
        loadAllComboData();

        List<Packet> packets = new LinkedList<>();

        for (PacketParams packetParams : packetParamsList) {
            Packet packet = new Packet();

            if (operationType == OperationType.UPDATE) {
                long packetId = packetParams.getId();
                packet = loadPacket(packetId);
                if (packet == null) {
                    LOGGER.info("Packet update. The packet with id " + packetId + " does not exist.");
                    continue;
                }
            }
            updateState(packet, packetParams.getStateId(), operationType);

            List<ComptParams> comptParamsList = packetParams.getComptParamsList();
            if (!comptParamsList.isEmpty()) {
                List<Compt> addedCompts = preparePacketAndComptsForSaving(packet, comptParamsList);
                if (operationType == OperationType.ADD) {
                    LOGGER.info("Adding the packets. The following compts for the new packet were added: " + addedCompts);
                } else if (operationType == OperationType.UPDATE) {
                    LOGGER.info("Packet update. The following compts for the packet#" + packet.getId() +
                            " were added: " + addedCompts);
                }
            }

            packets.add(packet);
        }
        packetRepository.save(packets).forEach(pkt -> {
            LOGGER.info(generateSavePacketReport(pkt, operationType));
        });
    }

    private String generateSavePacketReport(Packet pkt, OperationType operationType) {
        String report = "";
        if (operationType == OperationType.ADD) {
            report = "Adding the packet. Persisted new packet#" + pkt.getId() + " with the following state id: "
                    + pkt.getState().getId() + " and compts: " + pkt.getCompts();
        } else if (operationType == OperationType.UPDATE) {
            report = "Packet update. Merged packet#" + pkt.getId() + " with the following state id: " +
                    pkt.getState().getId() + " and compts: " + pkt.getCompts();
        }
        return report;
    }

    @Transactional
    private void updateState(Packet packet, Long stateId, OperationType operationType) {
        if (stateId != null) {
            State state = em.find(State.class, stateId);
            String report = "";
            if (state != null) {
                packet.setState(state);
                if (operationType == OperationType.UPDATE) {
                    report = "Packet update. The state for the packet#" + packet.getId() +
                            " was updated. The new state has the following id: " + stateId;
                } else if (operationType == OperationType.ADD) {
                    report = "Adding the packet. The state for the new packet was set. It has the following id: " + stateId;
                }
            } else {
                State defaultState = allStates.get(DEFAULT_STATE_INDEX);
                if (operationType == OperationType.UPDATE) {
                    report = "Packet update. Packet#" + packet.getId() + "state update. " +
                            "The state with id = " + stateId + "does not exist.";
                } else if (operationType == OperationType.ADD) {
                    packet.setState(defaultState);
                    report = "Adding the packet. The state with id = " + stateId + "does not exist.So it's " +
                            "automatically replaced with the state with id = " + DEFAULT_STATE_INDEX +
                            " and label: '" + defaultState.getLabel() + "'";
                }
            }
            LOGGER.info(report);
        }
    }

    @Transactional
    private List<Compt> preparePacketAndComptsForSaving(Packet packet, List<ComptParams> comptParamsList) {
        List<Compt> result = new ArrayList<>(comptParamsList.size());

        for (ComptParams comptParams : comptParamsList) {
            List<Integer> allComboDataIndeces;

            allComboDataIndeces = getIndicesFromVals(comptParams.getVals(), null, packet.getId());
            Compt newCompt = new Compt();
            newCompt.setLabel(comptParams.getLabel());
            packet.addCompt(newCompt);

            int numOfStates = allStates.size();
            int numOfComboDataItems = allComboData.size();

            for (int j = 0; j < numOfStates; j++) {
                for (int i = 0; i < numOfComboDataItems; i++) {
                    DataCompt dc = new DataCompt();
                    dc.setState(allStates.get(j));
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

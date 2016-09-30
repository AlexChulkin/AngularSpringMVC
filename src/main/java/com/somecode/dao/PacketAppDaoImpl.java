package com.somecode.dao;

import com.google.common.collect.Lists;
import com.somecode.domain.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

import static com.somecode.helper.Helper.getMessage;

@Service
@Repository
@Transactional(readOnly = true)
@Log4j
public class PacketAppDaoImpl implements PacketAppDao {
    private static final int DEFAULT_COMBO_DATA_INDEX = 0;
    private static final int DEFAULT_STATE_INDEX = 0;
    private static final String PACKET_ID = "packetId";
    private static final String LOAD_ALL_COMPTSSUPPLINFO_QUERY_NAME = "Compt.loadAllComptsSupplInfo";
    private static final String LOAD_COMPTSSUPPLINFO_BY_PACKETID_QUERY_NAME = "Compt.loadComptsSupplInfoByPacketId";
    private static final String ALL_PACKETS_LOADED = "packetAppDao.allPacketsLoaded";
    private static final String ALL_COMPTSSUPPLINFO_LOADED = "packetAppDao.allComptsSupplInfoLoaded";
    private static final String ALL_STATES_LOADED = "packetAppDao.allStatesLoaded";
    private static final String ALL_COMBODATA_LOADED = "packetAppDao.allComboDataLoaded";
    private static final String ALL_COMPTS_FROM_ALL_PACKETS_LOADED = "packetAppDao.allComptsFromAllPacketsLoaded";
    private static final String ALL_COMPTS_FROM_GIVEN_PACKET_LOADED = "packetAppDao.allComptsFromGivenPacketLoaded";
    private static final String NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT =
            "packetAppDao.nonExistingComboDataLabelErrorReport";
    private static final String NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT_COMPT_UPDATE =
            "packetAppDao.nonExistingComboDataLabelErrorReport.comptUpdate";
    private static final String NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT_ADD_NEW_COMPTS_TO_PERSISTED_PACKET =
            "packetAppDao.nonExistingComboDataLabelErrorReport.addNewComptsToPersistedPacket";
    private static final String NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT_ADD_NEW_COMPTS_TO_UNPERSISTED_PACKET =
            "packetAppDao.nonExistingComboDataLabelErrorReport.addNewComptsToUnpersistedPacket";
    private static final String MAP_COMBODATA_LABELS_TO_INDICES = "Map of comboData labels to indices: ";
    private static final String COMPT_UPDATE_NON_EXISTING_COMPT = "packetAppDao.comptUpdate.nonExistingCompt";
    private static final String COMPT_UPDATE_DATACOMPT_UPDATE =
            "packetAppDao.comptUpdate.dataComptUpdate.successReport";
    private static final String COMPT_UPDATE_SUCCESS_REPORT = "packetAppDao.comptUpdate.successReport";
    private static final String COMPTS_DELETE_NON_EXISTING_COMPTS = "packetAppDao.comptsDelete.nonExistingCompts";
    private static final String COMPTS_DELETE_SUCCESS_REPORT = "packetAppDao.comptsDelete.successReport";
    private static final String PACKETS_DELETE_NON_EXISTING_COMPTS = "packetAppDao.packetsDelete.nonExistingCompts";
    private static final String PACKETS_DELETE_SUCCESS_REPORT = "packetAppDao.packetsDelete.successReport";
    private static final String PACKET_ADD_OR_UPDATE_NOT_EXISTING_PACKET =
            "packetAppDao.packetAddOrUpdate.notExistingPacket";
    private static final String PACKET_ADDING_ADD_COMPTS = "packetAppDao.packetAddOrUpdate.addPacket.addCompts";
    private static final String PACKET_UPDATE_ADD_COMPTS = "packetAppDao.packetAddOrUpdate.updatePacket.addCompts";
    private static final String PACKET_ADDING_SUCCESS_REPORT = "packetAppDao.packetAddOrUpdate.addPacket.successReport";
    private static final String PACKET_UPDATE_SUCCESS_REPORT =
            "packetAppDao.packetAddOrUpdate.updatePacket.successReport";
    private static final String PACKET_UPDATE_STATE_UPDATE_SUCCESS_REPORT =
            "packetAppDao.packetAddOrUpdate.updatePacket.updateState.successReport";
    private static final String PACKET_ADDING_STATE_UPDATE_SUCCESS_REPORT =
            "packetAppDao.packetAddOrUpdate.addPacket.updateState.successReport";
    private static final String PACKET_UPDATE_STATE_UPDATE_NOT_EXISTING_STATE =
            "packetAppDao.packetAddOrUpdate.updatePacket.updateState.notExistingState";
    private static final String PACKET_ADDING_STATE_UPDATE_NOT_EXISTING_STATE =
            "packetAppDao.packetAddOrUpdate.addPacket.updateState.notExistingState";

    private List<ComboData> allComboData = Collections.EMPTY_LIST;
    private List<State> allStates = Collections.EMPTY_LIST;

    private Map<String, Integer> mapComboLabelsToIndices;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ComboDataRepository comboDataRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private ComptRepository comptRepository;

    @Autowired
    private PacketRepository packetRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Role getUserRole(String username, String password) {
        List<User> users = userRepository.findByUsername(username);
        if (!users.isEmpty()) {
            User user = users.get(0);
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user.getRole();
            }
        }
        return null;
    }

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
        log.debug(getMessage(ALL_PACKETS_LOADED,
                new Object[]{result.stream().map(PacketInfo::getId).collect(Collectors.toList())}));
        return result;
    }

    @Override
    public List<ComptSupplInfo> loadComptsSupplInfo(Long packetId) {
        List<ComptSupplInfo> result;
        if (packetId == null) {
            result = em.createNamedQuery(LOAD_ALL_COMPTSSUPPLINFO_QUERY_NAME, ComptSupplInfo.class)
                    .getResultList();
        } else {
            result = em.createNamedQuery(LOAD_COMPTSSUPPLINFO_BY_PACKETID_QUERY_NAME, ComptSupplInfo.class)
                    .setParameter(PACKET_ID, packetId)
                    .getResultList();
        }

        log.debug(getMessage(ALL_COMPTSSUPPLINFO_LOADED, new Object[]{result}));
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
            log.error(cause.getStackTrace());
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
        log.debug(getMessage(ALL_STATES_LOADED, new Object[]{allStates}));
        return allStates;
    }

    @Override
    public List<ComboData> loadAllComboData() throws DatabaseException {
        try {
            return loadAllComboDataLocally();
        } catch (EmptyDBTableException cause) {
            log.error(cause.getStackTrace());
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
        log.debug(getMessage(ALL_COMBODATA_LOADED, new Object[]{allComboData}));

        if (checkComboDataListsForEquality(allComboData, oldAllComboData)) {
            return allComboData;
        }
        mapComboLabelsToIndices = IntStream
                .range(0, allComboData.size())
                .boxed()
                .collect(Collectors.toMap(i -> allComboData.get(i).getLabel(), Function.identity()));

        log.debug(MAP_COMBODATA_LABELS_TO_INDICES + mapComboLabelsToIndices);

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
        log.debug(packetId == null
                ? getMessage(ALL_COMPTS_FROM_ALL_PACKETS_LOADED, new Object[]{result})
                : getMessage(ALL_COMPTS_FROM_GIVEN_PACKET_LOADED, new Object[]{packetId, result})
        );
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
            log.error(errorReport);
        }
        return result;
    }

    private String generateNonExistingComboDataLabelErrorReport(String label, Long comptId, Long packetId) {
        String errorReport = getMessage(NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT,
                new Object[]{label, allComboData.get(DEFAULT_COMBO_DATA_INDEX).getLabel()});
        StringBuilder sb = new StringBuilder();
        if (comptId != null) {
            errorReport = sb.append(getMessage(NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT_COMPT_UPDATE,
                    new Object[]{comptId})).append(errorReport).toString();
        } else if (packetId != null) {
            errorReport = sb.append(
                    getMessage(NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT_ADD_NEW_COMPTS_TO_PERSISTED_PACKET,
                            new Object[]{packetId})).append(errorReport).toString();
        } else {
            errorReport = sb.append(
                    getMessage(NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT_ADD_NEW_COMPTS_TO_UNPERSISTED_PACKET,
                            null)).append(errorReport).toString();
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
                log.error(getMessage(COMPT_UPDATE_NON_EXISTING_COMPT, new Object[]{comptId}));
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
                    log.debug(getMessage(COMPT_UPDATE_DATACOMPT_UPDATE, new Object[]{comptId, dc.getId()}));
                }
            }
            Long packetId = compt.getPacket().getId();
            if (result.get(packetId) == null) {
                result.put(packetId, new LinkedList<Long>());
            }
            result.get(packetId).add(comptId);
        }
        result.forEach((k, v) -> log.debug(getMessage(COMPT_UPDATE_SUCCESS_REPORT, new Object[]{k, v})));

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
            log.error(getMessage(COMPTS_DELETE_NON_EXISTING_COMPTS, new Object[]{idsToDelete}));
            return Collections.EMPTY_LIST;
        }

        comptRepository.delete(compts);
        List<Long> result = getIdsFromEntities(compts.toArray(new Compt[0]));
        log.debug(getMessage(COMPTS_DELETE_SUCCESS_REPORT, new Object[]{result}));

        if (comptsSize != idsToDelete.size()) {
            Set<Long> idsToDeleteSet = new HashSet<>(idsToDelete);
            log.error(getMessage(COMPTS_DELETE_NON_EXISTING_COMPTS,
                    new Object[]{idsToDeleteSet.removeAll(result)}));
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
            log.debug(getMessage(PACKETS_DELETE_NON_EXISTING_COMPTS, new Object[]{idsToDelete}));
            return Collections.EMPTY_LIST;
        }
        packetRepository.delete(packets);
        List<Long> result = getIdsFromEntities(packets.toArray(new Packet[0]));
        log.debug(getMessage(PACKETS_DELETE_SUCCESS_REPORT, new Object[]{result}));

        if (packetsSize != idsToDelete.size()) {
            Set<Long> idsToDeleteSet = new HashSet<>(idsToDelete);
            log.debug(getMessage(PACKETS_DELETE_NON_EXISTING_COMPTS, new Object[]{idsToDeleteSet.removeAll(result)}));
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
                    log.debug(getMessage(PACKET_ADD_OR_UPDATE_NOT_EXISTING_PACKET, new Object[]{packetId}));
                    continue;
                }
            }
            updateState(packet, packetParams.getStateId(), operationType);

            List<ComptParams> comptParamsList = packetParams.getComptParamsList();
            if (!comptParamsList.isEmpty()) {
                List<Compt> addedCompts = preparePacketAndComptsForSaving(packet, comptParamsList);
                if (operationType == OperationType.ADD) {
                    log.debug(getMessage(PACKET_ADDING_ADD_COMPTS,
                            new Object[]{addedCompts}));
                } else if (operationType == OperationType.UPDATE) {
                    log.debug(getMessage(PACKET_UPDATE_ADD_COMPTS, new Object[]{packet.getId(), addedCompts}));
                }
            }

            packets.add(packet);
        }
        packetRepository.save(packets).forEach(pkt -> {
            log.debug(generateSavePacketReport(pkt, operationType));
        });
    }

    private String generateSavePacketReport(Packet pkt, OperationType operationType) {
        String report = "";
        if (operationType == OperationType.ADD) {
            report = getMessage(PACKET_ADDING_SUCCESS_REPORT,
                    new Object[]{pkt.getId(), pkt.getState().getId(), pkt.getCompts()});
        } else if (operationType == OperationType.UPDATE) {
            report = getMessage(PACKET_UPDATE_SUCCESS_REPORT,
                    new Object[]{pkt.getId(), pkt.getState().getId(), pkt.getCompts()});
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
                    report = getMessage(PACKET_UPDATE_STATE_UPDATE_SUCCESS_REPORT,
                            new Object[]{packet.getId(), stateId});
                } else if (operationType == OperationType.ADD) {
                    report = getMessage(PACKET_ADDING_STATE_UPDATE_SUCCESS_REPORT,
                            new Object[]{stateId});
                }
            } else {
                State defaultState = allStates.get(DEFAULT_STATE_INDEX);
                if (operationType == OperationType.UPDATE) {
                    report = getMessage(PACKET_ADDING_STATE_UPDATE_NOT_EXISTING_STATE,
                            new Object[]{packet.getId(), stateId});
                } else if (operationType == OperationType.ADD) {
                    packet.setState(defaultState);
                    report = getMessage(PACKET_UPDATE_STATE_UPDATE_NOT_EXISTING_STATE,
                            new Object[]{stateId, DEFAULT_STATE_INDEX, defaultState.getLabel()});
                }
            }
            log.debug(report);
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

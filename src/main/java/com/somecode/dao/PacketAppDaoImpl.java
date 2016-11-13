
/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.dao;

import com.google.common.collect.Lists;
import com.somecode.domain.*;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.somecode.utils.Utils.getMessage;

/**
 * The DAO class implementing the full functionality of the interaction with model.
 * @version 1.0
 */
@Repository
@Transactional(readOnly = true)
@Log4j
public class PacketAppDaoImpl implements PacketAppDao {
    /**
     * The default combo data index used when we've got some weird combo data and want to replace it with default one.
     */
    private static final int DEFAULT_COMBO_DATA_INDEX = 0;

    /**
     * The default state index used when we've got some weird combo data and want to replace it with default one.
     */
    private static final int DEFAULT_STATE_INDEX = 0;

    /**
     * The message source constants
     */
    private static final String PACKET_ID = "packetId";
    private static final String LOAD_ALL_COMPTSSUPPLINFO_QUERY_NAME = "Compt.loadAllComptsSupplInfo";
    private static final String LOAD_COMPTSSUPPLINFO_BY_PACKETID_QUERY_NAME = "Compt.loadComptsSupplInfoByPacketId";
    private static final String ALL_PACKETS_LOADED_MESSAGE = "packetAppDao.allPacketsLoadedMessage";
    private static final String ALL_COMPTSSUPPLINFO_LOADED = "packetAppDao.allComptsSupplInfoLoadedMessage";
    private static final String ALL_STATES_LOADED_MESSAGE = "packetAppDao.allStatesLoadedMessage";
    private static final String ALL_COMBODATA_LOADED_MESSAGE = "packetAppDao.allComboDataLoadedMessage";
    private static final String ALL_COMPTS_FROM_ALL_PACKETS_LOADED_MESSAGE
            = "packetAppDao.allComptsFromAllPacketsLoadedMessage";
    private static final String ALL_COMPTS_FROM_SPECIFIC_PACKET_LOADED_MESSAGE
            = "packetAppDao.allComptsFromSpecificPacketLoadedMessage";
    private static final String NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT
            = "packetAppDao.nonExistingComboDataLabelErrorReport";
    private static final String NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT_COMPT_UPDATE
            = "packetAppDao.nonExistingComboDataLabelErrorReport.comptUpdate";
    private static final String NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT_ADD_NEW_COMPTS_TO_PERSISTED_PACKET
            = "packetAppDao.nonExistingComboDataLabelErrorReport.addNewComptsToPersistedPacket";
    private static final String NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT_ADD_NEW_COMPTS_TO_UNPERSISTED_PACKET
            = "packetAppDao.nonExistingComboDataLabelErrorReport.addNewComptsToUnpersistedPacket";
    private static final String MAP_COMBODATA_LABELS_TO_INDICES_MESSAGE = "packetAppDao.allComboDataLabelsToIndicesMap";
    private static final String COMPT_UPDATE_NON_EXISTING_COMPT = "packetAppDao.comptUpdate.nonExistingCompt";
    private static final String COMPT_UPDATE_DATACOMPT_UPDATE
            = "packetAppDao.comptUpdate.dataComptUpdate.successReport";
    private static final String COMPT_UPDATE_SUCCESS_REPORT = "packetAppDao.comptUpdate.successReport";
    private static final String COMPTS_DELETE_NON_EXISTING_COMPTS = "packetAppDao.comptsDelete.nonExistingCompts";
    private static final String COMPTS_DELETE_SUCCESS_REPORT = "packetAppDao.comptsDelete.successReport";
    private static final String PACKETS_DELETE_NON_EXISTING_IDS = "packetAppDao.packetsDelete.nonExistingIds";
    private static final String PACKETS_DELETE_SUCCESS_REPORT = "packetAppDao.packetsDelete.successReport";
    private static final String PACKET_UPDATE_NOT_EXISTING_PACKET
            = "packetAppDao.packetUpdate.notExistingPacket";
    private static final String PACKET_ADDING_ADD_COMPTS = "packetAppDao.packetAddOrUpdate.addPacket.addCompts";
    private static final String PACKET_UPDATE_ADD_COMPTS = "packetAppDao.packetAddOrUpdate.updatePacket.addCompts";
    private static final String PACKET_ADDING_SUCCESS_REPORT = "packetAppDao.packetAddOrUpdate.addPacket.successReport";
    private static final String PACKET_UPDATE_SUCCESS_REPORT
            = "packetAppDao.packetAddOrUpdate.updatePacket.successReport";
    private static final String PACKET_UPDATE_STATE_UPDATE_SUCCESS_REPORT
            = "packetAppDao.packetAddOrUpdate.updatePacket.updateState.successReport";
    private static final String PACKET_ADDING_STATE_SET_SUCCESS_REPORT
            = "packetAppDao.packetAddOrUpdate.addPacket.setState.successReport";
    private static final String PACKET_UPDATE_STATE_UPDATE_NULL_NEW_STATE
            = "packetAppDao.packetAddOrUpdate.updatePacket.updateState.nullNewState";
    private static final String PACKET_UPDATE_STATE_UPDATE_NOT_EXISTING_STATE
            = "packetAppDao.packetAddOrUpdate.updatePacket.updateState.notExistingState";
    private static final String PACKET_ADDING_STATE_SET_NOT_EXISTING_STATE
            = "packetAppDao.packetAddOrUpdate.addPacket.setState.notExistingState";
    private static final String STATE_TABLE_IS_EMPTY = "packetAppDao.statesTableIsEmpty";
    private static final String COMBODATA_TABLE_IS_EMPTY = "packetAppDao.comboDataTableIsEmpty";
    private static final String PACKET_NOT_LOADED = "packetAppDao.packetNotLoaded";
    private static final String NO_PACKETS_LOADED = "packetAppDao.noPacketsLoaded";
    private static final String PACKET_ADDING_STATE_SET_NULL_STATE
            = "packetAppDao.packetAddOrUpdate.addPacket.setState.nullState";
    private static final String PACKET_UPDATE_STATE_UPDATE_NOT_DIFFERENT_NEW_STATE
            = "packetAppDao.packetAddOrUpdate.updatePacket.updateState.notDifferentNewState";
    private static final String USER_DATA_LOAD_SUCCESS = "packetAppDao.userDataLoad.success";
    private static final String USER_DATA_LOAD_ERROR = "packetAppDao.userDataLoad.error";

    /** The full combo data list. */
    private List<ComboData> allComboData = Collections.emptyList();
    /** The full state list. */
    private List<State> allStates = Collections.emptyList();

    /**
     * The map connecting all the combo labels to their unique indeces. Used when we obtain the comboLabels list from
     * the front-end and need to transform this to indeces.
     */
    private Map<String, Integer> mapComboLabelsToIndices;

    /**
     * Empty DB Table messages and stack traces used for testing convenience.
     */
    private String emptyStateTableExceptionMessage;
    private String emptyComboDataTableExceptionMessage;
    private String emptyStateTableExceptionStackTrace;
    private String emptyComboDataTableExceptionStackTrace;

    /** A simple entity manager.   */
    @PersistenceContext
    private EntityManager em;

    /** The Spring Data repositories. */
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

    /**
     * Gets the user role corresponding to the given credentials if any or null.
     *
     * @param username the user name input.
     * @param password the user password input.
     * @return role correspondent to the parameters or null.
     */
    @Override
    public Role getUserRole(String username, String password) {
        List<User> users = userRepository.findByUsername(username);
        if (!users.isEmpty()) {
            User user = users.get(0);
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                log.info(getMessage(USER_DATA_LOAD_SUCCESS, new Object[]{username}));
                return user.getRole();
            }
        }
        log.error(getMessage(USER_DATA_LOAD_ERROR, new Object[]{username}));
        return null;
    }

    /**
     * Returns the list of the {@link PacketInfo} instances (brief version of the Packet entity) for all the
     * {@literal Packet}  instances already persisted to the DB if the {@literal packetId} parameter is null or the list
     * containing the single {@link PacketInfo} instance, otherwise (corresponding to the {@literal packetId} in this case)
     *
     * @param packetId the id of the packet if the single {@link PacketInfo} is to be found or null if the
     *                 {@link PacketInfo} views of all the persisted {@link Packet} entities are to be found
     * @return list of the {@link PacketInfo}
     */
    @Override
    public List<PacketInfo> loadPackets(Long packetId) {
        List<PacketInfo> result = new LinkedList<>();
        if (Optional.ofNullable(packetId).isPresent()) {
            Packet onlyResult = packetRepository.findOne(packetId);
            if (!Optional.ofNullable(onlyResult).isPresent()) {
                log.error(getMessage(PACKET_NOT_LOADED, new Object[]{packetId}));
                return Collections.unmodifiableList(result);
            }
            result.add(new PacketInfo(onlyResult));
        } else {
            packetRepository.findAll().forEach(p -> result.add(new PacketInfo(p)));
            if (result.isEmpty()) {
                log.error(getMessage(NO_PACKETS_LOADED, null));
                return Collections.unmodifiableList(result);
            }
        }
        log.info(getMessage(ALL_PACKETS_LOADED_MESSAGE,
                new Object[]{result.stream().map(PacketInfo::getId).collect(Collectors.toList())}));
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns the list of the ComptSupplInfo entities (they contain the supplementary info containing the details of
     *  the compt entity connection to the other entities like ComboData, Datacompt, etc) corresponding to the given
     *  {@literal #packetId}.
     *
     * @param packetId the id of the packet the compts of which we are going to find.
     * @return the list of the ComptSupplInfo instances.
     * @throws org.springframework.dao.QueryTimeoutException if the query execution exceeds the query timeout value set
     *         and only the statement is rolled back
     * @throws javax.persistence.PersistenceException if the query execution exceeds the query timeout value set and
     *         the transaction is rolled back
     */
    @Override
    public List<ComptSupplInfo> loadComptsSupplInfo(Long packetId) {
        List<ComptSupplInfo> result;
        if (!Optional.ofNullable(packetId).isPresent()) {
            result = em.createNamedQuery(LOAD_ALL_COMPTSSUPPLINFO_QUERY_NAME, ComptSupplInfo.class)
                    .getResultList();
        } else {
            result = em.createNamedQuery(LOAD_COMPTSSUPPLINFO_BY_PACKETID_QUERY_NAME, ComptSupplInfo.class)
                    .setParameter(PACKET_ID, packetId)
                    .getResultList();
        }

        log.info(getMessage(ALL_COMPTSSUPPLINFO_LOADED, new Object[]{result}));
        return Collections.unmodifiableList(result);
    }

    /**
     * Loads the packet from the DB using its id.
     *
     * @param packetId the packet id.
     * @throws IllegalArgumentException - if the {@literal packetId} is null.
     * @return the {@link Packet} entity.
     */
    private Packet loadPacket(Long packetId) {
        return em.find(Packet.class, packetId);
    }

    /**
     * Loads all the entities from the STATE table.
     *
     * @return List of the {@link State} entities found.
     * @throws DatabaseException if the STATE table is empty.
     */
    @Override
    public List<State> loadAllStates() throws DatabaseException {
        try {
            return loadAllStatesLocally();
        } catch (EmptyStateTableException cause) {
            throw logDBErrorAndReturnDBTableException(cause);
        }
    }

    /**
     * Returns the upper-level {@link DatabaseException} chained with the causing {@link EmptyDBTableException}
     * and logs the error.
     *
     * @param cause the causing {@link EmptyDBTableException}.
     * @return DatabaseException instance chained with the cause.
     */
    private DatabaseException logDBErrorAndReturnDBTableException(EmptyDBTableException cause) {
        if (cause.getClass().equals(EmptyStateTableException.class)) {
            emptyStateTableExceptionMessage = cause.getMessage();
            emptyStateTableExceptionStackTrace = ExceptionUtils.getStackTrace(cause);
            log.error(getMessage(STATE_TABLE_IS_EMPTY,
                    new Object[]{emptyStateTableExceptionMessage, emptyStateTableExceptionStackTrace}));
        } else if (cause.getClass().equals(EmptyComboDataTableException.class)) {
            emptyComboDataTableExceptionMessage = cause.getMessage();
            emptyComboDataTableExceptionStackTrace = ExceptionUtils.getStackTrace(cause);
            log.error(getMessage(COMBODATA_TABLE_IS_EMPTY,
                    new Object[]{emptyComboDataTableExceptionMessage,
                            emptyComboDataTableExceptionStackTrace}));
        }
        DatabaseException exc = new DatabaseException();
        exc.initCause(cause);
        return exc;
    }

    /**
     * Returns the list containing all the STATE entities from the DB.
     *
     * @return the State entities list
     * @throws EmptyStateTableException if the STATE table is empty.
     */
    private List<State> loadAllStatesLocally() throws EmptyStateTableException {
        Iterable<State> iterable = stateRepository.findAll();
        allStates = Lists.newArrayList(iterable);
        if (allStates.isEmpty()) {
            throw new EmptyStateTableException();
        }
        log.info(getMessage(ALL_STATES_LOADED_MESSAGE, new Object[]{allStates}));
        return Collections.unmodifiableList(allStates);
    }

    /**
     * Loads all the entities from the COMBO_DATA table.
     *
     * @return List of the {@link ComboData} entities found.
     * @throws DatabaseException if the COMBO_DATA table is empty.
     */
    @Override
    public List<ComboData> loadAllComboData() throws DatabaseException {
        try {
            return loadAllComboDataLocally();
        } catch (EmptyDBTableException cause) {
            throw logDBErrorAndReturnDBTableException(cause);
        }
    }

    /**
     * Returns the list containing all the COMBO_DATA entities from the DB.
     *
     * @return the ComboData entities list
     * @throws EmptyComboDataTableException if the COMBO_DATA table is empty.
     */
    private List<ComboData> loadAllComboDataLocally() throws EmptyComboDataTableException {
        List<ComboData> oldAllComboData = allComboData;
        allComboData = Lists.newArrayList(comboDataRepository.findAll());
        if (allComboData.isEmpty()) {
            throw new EmptyComboDataTableException();
        }
        log.info(getMessage(ALL_COMBODATA_LOADED_MESSAGE, new Object[]{allComboData}));

        if (checkComboDataListsForEquality(allComboData, oldAllComboData)) {
            return Collections.unmodifiableList(allComboData);
        }
        mapComboLabelsToIndices = IntStream
                .range(0, allComboData.size())
                .boxed()
                .collect(Collectors.toMap(i -> allComboData.get(i).getLabel(),
                        Function.identity()));

        log.info(getMessage(MAP_COMBODATA_LABELS_TO_INDICES_MESSAGE, new Object[]{mapComboLabelsToIndices}));

        return Collections.unmodifiableList(allComboData);
    }

    /**
     * Checks the combo data lists for equality and returns true if they are equal, or false - if not
     * @param newAllComboData the new combo data list
     * @param oldAllComboData the old combo data list
     * @return true if the lists are equal, or false - if not
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private boolean checkComboDataListsForEquality(List<ComboData> newAllComboData,
                                                   List<ComboData> oldAllComboData) {
        int size = newAllComboData.size();
        return (size == oldAllComboData.size()) && IntStream.range(0, size).boxed()
                .allMatch(i -> getSortedLabels(newAllComboData).get(i).equals(getSortedLabels(oldAllComboData).get(i)));
    }

    /**
     * Returns the sorted list of the labels collected from the given combo data list.
     *
     * @param comboDataList the combo data list the labels of which are to be sorted.
     * @return the equality result.
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private List<String> getSortedLabels(List<ComboData> comboDataList) {
        List<String> result = comboDataList.stream().map(ComboData::getLabel).sorted().collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns the list of the ComptInfo entities (brief version of Compt) corresponding to the given Packet id.
     * @param packetId the id of the packet whose compts we are going to find.
     * @return the list of the ComptInfo entities.
     */
    @Override
    public List<ComptInfo> loadCompts(Long packetId) {
        List<ComptInfo> result;
        boolean packetIdIsNotNull = Optional.ofNullable(packetId).isPresent();
        if (packetIdIsNotNull) {
            result = comptRepository.findByPacket_Id(packetId).stream().map(ComptInfo::new).collect(Collectors.toList());
        } else {
            List<ComptInfo> listOfCompts = new LinkedList<>();
            comptRepository.findAll().forEach(c -> listOfCompts.add(new ComptInfo(c)));
            result = listOfCompts;
        }
        log.info(packetIdIsNotNull
                ? getMessage(ALL_COMPTS_FROM_SPECIFIC_PACKET_LOADED_MESSAGE, new Object[]{packetId, result})
                : getMessage(ALL_COMPTS_FROM_ALL_PACKETS_LOADED_MESSAGE, new Object[]{result})
        );
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns the indices list corresponging to the labels.
     *
     * @param labels the labels list
     * @param comptId the id of the compt that is going to be updated.
     * @param packetId the id of the packet that is going to be added or updated.
     * @return the indices list.
     */
    @Transactional
    private List<Integer> getIndicesFromVals(List<String> labels, Long comptId, Long packetId) {
        List<Integer> result
                = labels.stream().map(v -> mapLabelToIndex(v, comptId, packetId)).collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns the index corresponding to the given combo data label or the default index if no index is found.
     *
     * @param label the label, index of what is to be found.
     * @param comptId the compt id, used for generating the error report if the resulting index is null.
     * @param packetId the packet id, used for generating the error report if the resulting index is null.
     * @return the label index.
     */
    private Integer mapLabelToIndex(String label, Long comptId, Long packetId) {
        Integer result = mapComboLabelsToIndices.get(label);
        if (!Optional.ofNullable(result).isPresent()) {
            result = DEFAULT_COMBO_DATA_INDEX;
            String errorReport = generateNonExistingComboDataLabelErrorReport(label, comptId, packetId);
            log.error(errorReport);
        }
        return result;
    }

    /**
     * Generates and returns the error report for the combo data label that does not exist in the DB.
     *
     * @param label the non-existing label.
     * @param comptId the compt id, it is not null if the error occurred during the compt update.
     * @param packetId the packet id, it is not null if the error occurred during adding the compts to the
     *                 persisted packet and it is null if the error occurred during adding the compts to the unpersisted
     *                 packet (if the {@param comptId} is null) or if it's not needed (if the {@param comptId} is null).
     * @return the generated error report.
     */
    private String generateNonExistingComboDataLabelErrorReport(String label, Long comptId, Long packetId) {
        String errorReport = getMessage(NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT,
                new Object[]{label, allComboData.get(DEFAULT_COMBO_DATA_INDEX).getLabel()});
        StringBuilder sb = new StringBuilder();
        if (Optional.ofNullable(comptId).isPresent()) {
            errorReport = sb.append(getMessage(NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT_COMPT_UPDATE,
                    new Object[]{comptId})).append(errorReport).toString();
        } else if (Optional.ofNullable(packetId).isPresent()) {
            errorReport = sb.append(
                    getMessage(NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT_ADD_NEW_COMPTS_TO_PERSISTED_PACKET,
                            new Object[]{packetId}))
                    .append(errorReport).toString();
        } else {
            errorReport = sb.append(
                    getMessage(NON_EXISTING_COMBODATA_LABEL_ERROR_REPORT_ADD_NEW_COMPTS_TO_UNPERSISTED_PACKET, null))
                    .append(errorReport).toString();
        }
        return errorReport;
    }

    /**
     * Updates the given compts and returns the map containing the packet ids as keys and the corresponding lists of the
     * compt ids as values
     *
     * @param comptParamsList list of the {@link ComptParams} instances containing all the necessary info about update
     * @return map containing the packet ids as keys and the corresponding lists of the
     * compt ids as values
     * @throws DatabaseException if the COMBO_DATA table is empty
     * @throws IllegalArgumentException if any of the compt ids from the {@param comptParamsList} is null
     * @throws javax.persistence.OptimisticLockException if the optimistic version check for any {@link Compt}
     *         id in question fails
     */
    @Override
    @Transactional
    public Map<Long, List<Long>> updateCompts(List<ComptParams> comptParamsList) throws DatabaseException {
        loadAllComboData();
        Map<Long, List<Long>> result = new HashMap<>();

        for (ComptParams comptParams : comptParamsList) {
            Long comptId = comptParams.getId();
            Compt compt = em.find(Compt.class, comptId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            if (!Optional.ofNullable(compt).isPresent()) {
                log.error(getMessage(COMPT_UPDATE_NON_EXISTING_COMPT, new Object[]{comptId}));
                continue;
            }
            List<Integer> newCheckedIndices = getIndicesFromVals(comptParams.getVals(), comptId, null);
            List<DataCompt> dataCompts = compt.getDataCompts();
            for (DataCompt dc : dataCompts) {
                int stateIndex = (int) (dc.getState().getId() - 1);
                int comboDataIndex = (int) (dc.getComboData().getId() - 1);
                boolean checked = dc.getChecked();
                if (!checked && newCheckedIndices.get(stateIndex) == comboDataIndex
                        || checked && newCheckedIndices.get(stateIndex) != comboDataIndex) {
                    dc.setChecked(!checked);
                    log.info(getMessage(COMPT_UPDATE_DATACOMPT_UPDATE, new Object[]{comptId, dc.getId()}));
                }
            }
            Long packetId = compt.getPacket().getId();
            result.putIfAbsent(packetId, new LinkedList<>());
            result.get(packetId).add(comptId);
        }
        result.forEach((k, v) -> log.info(getMessage(COMPT_UPDATE_SUCCESS_REPORT, new Object[]{k, v})));

        return Collections.unmodifiableMap(result);
    }

    /**
     * Deletes the compts from the DB.
     *
     * @param idsToDelete the ids of the compts that are to be deleted.
     * @return the list of the ids of the actually deleted compts.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> deleteCompts(List<Long> idsToDelete) {
        List<Compt> compts = comptRepository
                .findByIdIn(idsToDelete)
                .stream()
                .filter(c -> Optional.ofNullable(c).isPresent())
                .collect(Collectors.toList());

        int comptsSize = compts.size();
        if (comptsSize == 0) {
            log.error(getMessage(COMPTS_DELETE_NON_EXISTING_COMPTS, new Object[]{idsToDelete}));
            return Collections.emptyList();
        }

        comptRepository.delete(compts);
        List<Long> result = getIdsFromEntities(compts.toArray(new Compt[0]));
        log.info(getMessage(COMPTS_DELETE_SUCCESS_REPORT, new Object[]{result}));

        if (comptsSize != idsToDelete.size()) {
            Set<Long> idsToDeleteSet = new HashSet<>(idsToDelete);
            idsToDeleteSet.removeAll(result);
            log.error(getMessage(COMPTS_DELETE_NON_EXISTING_COMPTS,
                    new Object[]{idsToDeleteSet}));
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * Returns the list of the entities ids.
     * @param entities the entities list.
     * @return the list of the ids.
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private List<Long> getIdsFromEntities(EntityType[] entities) {
        List<Long> result = Arrays.stream(entities).mapToLong(EntityType::getId).boxed().collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    /**
     * Deletes the packets from the DB.
     *
     * @param idsToDelete the list of the ids of the compts that are to be deleted.
     * @return the list of the ids of the actually deleted compts.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> deletePackets(List<Long> idsToDelete) {
        List<Packet> packets = packetRepository
                .findByIdIn(idsToDelete)
                .stream()
                .filter(p -> Optional.ofNullable(p).isPresent())
                .collect(Collectors.toList());

        int packetsSize = packets.size();
        if (packetsSize == 0) {
            log.error(getMessage(PACKETS_DELETE_NON_EXISTING_IDS, new Object[]{idsToDelete}));
            return Collections.emptyList();
        }
        packetRepository.delete(packets);
        List<Long> result = getIdsFromEntities(packets.toArray(new Packet[0]));
        log.info(getMessage(PACKETS_DELETE_SUCCESS_REPORT, new Object[]{result}));

        if (packetsSize != idsToDelete.size()) {
            Set<Long> idsToDeleteSet = new HashSet<>(idsToDelete);
            log.info(getMessage(PACKETS_DELETE_NON_EXISTING_IDS, new Object[]{idsToDeleteSet.removeAll(result)}));
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * Adds or updates the packet in the DB.
     *
     * @param packetParamsList list of the packet params containinf the packet info.
     * @param operationType the operation type (ADD or UPDATE).
     * @throws DatabaseException if the STATE or COMBO_DATA table is empty.
     * @throws javax.persistence.TransactionRequiredException if the entity manager has not been joined to the current
     *         transaction.
     * @throws javax.persistence.PersistenceException - if the flush fails.
     */
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
                if (!Optional.ofNullable(packet).isPresent()) {
                    log.error(getMessage(PACKET_UPDATE_NOT_EXISTING_PACKET, new Object[]{packetId}));
                    continue;
                }
            }

            boolean updated = setOrUpdateState(packet, packetParams.getStateId(), operationType);

            List<ComptParams> comptParamsList = packetParams.getComptParamsList();
            if (!comptParamsList.isEmpty()) {
                List<Compt> addedCompts = preparePacketAndComptsForSaving(packet, comptParamsList);
                if (operationType == OperationType.ADD) {
                    log.info(getMessage(PACKET_ADDING_ADD_COMPTS, new Object[]{addedCompts}));
                } else if (operationType == OperationType.UPDATE) {
                    log.info(getMessage(PACKET_UPDATE_ADD_COMPTS, new Object[]{packet.getId(), addedCompts}));
                }
                updated = true;
            }
            if (updated) {
                packets.add(packet);
            }
        }
        packetRepository.save(packets);
        em.flush();
        packets.forEach(pkt -> log.info(generateSavePacketReport(pkt, operationType)));
    }

    /**
     *  Returns the report about the packet saving/updating to the DB
     *
     * @param pkt the persisted/merged packet
     * @param operationType the operation type: ADD(save) or UPDATE
     * @return the report
     */
    private String generateSavePacketReport(Packet pkt, OperationType operationType) {
        String report = null;

        if (operationType == OperationType.ADD) {
            report = getMessage(PACKET_ADDING_SUCCESS_REPORT, new Object[]{pkt});
        } else if (operationType == OperationType.UPDATE) {
            report = getMessage(PACKET_UPDATE_SUCCESS_REPORT, new Object[]{pkt});
        }
        return report;
    }

    /**
     * Either sets/updates the given packet's state, returning true, or doesn't do anything and returns false if the
     * state update is not possible
     *
     * @param packet the packet a state of which could be changed/set
     * @param stateId the new state id
     * @param operationType the packet operation type, may be ADD or UPDATE. Used for the logging.
     * @return true if the given packet's state is changed in the result of the method's execution, otherwise returns
     *         false. The latter could happen if the state is updated and:
     *         (a) the new state is the same as the old one;
     *         (b) the new state is null and (c) the new state does not exist in the DB.
     */
    @Transactional
    private boolean setOrUpdateState(Packet packet, Long stateId, OperationType operationType) {
        String report = "";
        Long packetId = packet.getId();
        if (!Optional.ofNullable(stateId).isPresent()) {
            return setDefaultState(operationType, PacketDaoErrorType.NULL_NEW_STATE_ID, packet, null);
        } else if (operationType == OperationType.UPDATE && packet.getState().getId().equals(stateId)) {
            report = getMessage(PACKET_UPDATE_STATE_UPDATE_NOT_DIFFERENT_NEW_STATE, new Object[]{stateId});
            log.error(report);
            return false;
        }
        State state = em.find(State.class, stateId);
        if (Optional.ofNullable(state).isPresent()) {
            packet.setState(state);
            if (operationType == OperationType.UPDATE) {
                report = getMessage(PACKET_UPDATE_STATE_UPDATE_SUCCESS_REPORT,
                        new Object[]{packetId, stateId});
            } else if (operationType == OperationType.ADD) {
                report = getMessage(PACKET_ADDING_STATE_SET_SUCCESS_REPORT,
                        new Object[]{packetId, stateId});
            }
            log.info(report);
            return true;
        } else {
            return setDefaultState(operationType, PacketDaoErrorType.NOT_EXISTING_STATE_ID, packet, stateId);
        }
    }

    /**
     * Checks if the default state could be set for the given packet.
     * If yes, then sets it and returns true. Otherwise returns false.
     *
     * @param operationType the operation type: ADD or UPDATE.
     * @param error         the error type, can be either NOT_EXISTING_STATE_ID or NULL_NEW_STATE_ID here.
     * @param packet        the given packet.
     * @param stateId       the intended new/updated state id.
     * @return true if the packet was uodated, false - otherwise.
     */
    private boolean setDefaultState(OperationType operationType, PacketDaoErrorType error, Packet packet,
                                    Long stateId) {
        String report = null;
        if (operationType == OperationType.ADD) {
            State defaultState = allStates.get(DEFAULT_STATE_INDEX);
            packet.setState(defaultState);
            if (error == PacketDaoErrorType.NOT_EXISTING_STATE_ID) {
                report = getMessage(PACKET_ADDING_STATE_SET_NOT_EXISTING_STATE,
                        new Object[]{stateId, defaultState.toString()});
            } else if (error == PacketDaoErrorType.NULL_NEW_STATE_ID) {
                report = getMessage(PACKET_ADDING_STATE_SET_NULL_STATE, new Object[]{defaultState.toString()});
            }
            log.info(report);
            return true;
        } else if (operationType == OperationType.UPDATE) {
            if (error == PacketDaoErrorType.NULL_NEW_STATE_ID) {
                report = getMessage(PACKET_UPDATE_STATE_UPDATE_NULL_NEW_STATE, new Object[]{packet.getId()});
            } else if (error == PacketDaoErrorType.NOT_EXISTING_STATE_ID) {
                report = getMessage(PACKET_UPDATE_STATE_UPDATE_NOT_EXISTING_STATE, new Object[]{packet.getId(),
                        stateId});
            }
            log.error(report);
        }
        return false;
    }

    /**
     * Prepares the given packet and compts for saving and return the list of compts that are ready to be saved.
     *
     * @param packet the packet to prepare.
     * @param comptParamsList the list of the params of compts to prepare.
     * @return list of the prepared compts.
     */
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
        return Collections.unmodifiableList(result);
    }
}

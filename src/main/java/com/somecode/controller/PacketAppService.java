/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.controller;

import com.somecode.dao.PacketAppDao;
import com.somecode.domain.*;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.somecode.utils.Utils.getMessage;

/**
 * The service connecting the restful controller and dao.
 * @version 1.0
 */

@Service("packetAppService")
@Log4j
public class PacketAppService {
    /**
     * The source messages
     */
    private static final String LOAD_DATA_FOR_ALL_PACKETS = "packetAppService.loadDataForAllPackets";
    private static final String LOAD_DATA_FOR_SPECIFIC_PACKET = "packetAppService.loadDataForSpecificPacket";
    private static final String PERSIST_ALL_PACKETS = "packetAppService.persistDataForAllPackets";
    private static final String PERSIST_SPECIFIC_PACKET = "packetAppService.persistDataForSpecificPacket";
    private static final String UPDATE_COMPTS_EXCEPTION_REPORT = "packetAppService.updateCompts.exceptionReport";
    private static final String UPDATE_PACKETS_EXCEPTION_REPORT = "packetAppService.updatePackets.exceptionReport";
    private static final String ADD_PACKETS_EXCEPTION_REPORT = "packetAppService.addPackets.exceptionReport";

    /**
     * The String constants
     */
    private static final String UPDATE_COMPTS = "UPDATE_COMPTS";
    private static final String UPDATE_PACKETS = "UPDATE_PACKETS";
    private static final String ADD_PACKETS = "ADD_PACKETS";

    /**
     * The exceptions' stack trace and message info, used for the testing
     */
    private String updateComptsExceptionStackTrace;
    private String updatePacketsExceptionStackTrace;
    private String addPacketsExceptionStackTrace;
    private String updateComptsExceptionMessage;
    private String updatePacketsExceptionMessage;
    private String addPacketsExceptionMessage;

    /** The dao injected */
    @Autowired
    private PacketAppDao packetAppDao;

    /**
     * Returns the result of dao  {@link com.somecode.dao #getUserRole(String, String)} method.
     *
     * @param username the user name input.
     * @param password the user password input.
     * @return role correspondent to the parameters.
     */
    public Role getUserRole(String username, String password) {
        return packetAppDao.getUserRole(username, password);
    }

    /**
     * Loads the data from the back-end to the front-end.
     *
     * @param packetId the {@link Packet} id that is to be loaded or null if all the packets are to be loaded.
     * @return the {@link Data} instance containing {@link Packet}s, {@link Compt}s, {@link State}s and
     *         {@link ComboData}s together with the {@link ComptSupplInfo}s.
     */
    public Data loadData(Long packetId) {
        log.info(Optional.ofNullable(packetId).isPresent()
                ? getMessage(LOAD_DATA_FOR_SPECIFIC_PACKET, new Object[]{packetId})
                : getMessage(LOAD_DATA_FOR_ALL_PACKETS, null));

        Data.Builder result = Data.createBuilder();
        List<State> states;

        try {
            states = packetAppDao.loadAllStates();
        } catch (DatabaseException e) {
            states = Collections.emptyList();
        }

        List<ComboData> comboDatas;

        try {
            comboDatas = packetAppDao.loadAllComboData();
        } catch (DatabaseException e) {
            comboDatas = Collections.emptyList();
        }

        List<ComptInfo> compts = packetAppDao.loadCompts(packetId);

        result.comboData(comboDatas).states(states).packets(packetAppDao.loadPackets(packetId)).compts(compts);

        if (!compts.isEmpty() && !states.isEmpty() && !states.isEmpty()) {
            result.comptSupplInfo(packetAppDao.loadComptsSupplInfo(packetId));
        } else {
            result.comptSupplInfo(Collections.emptyList());
        }

        return result.build();
    }

    /**
     * The method saves the front-end data to the DB and returns the immutable error map.
     *
     * @param comptIdsToDelete list of the {@link Compt} ids that are to be saved.
     * @param packetIdsToDelete list of the {@link Packet} ids that are to be saved. Has no meaning if the
     *                          {@param packetId} is not null.
     * @param comptsToUpdateParamsList list of the {@link ComptParams} representing the new {@link Compt}s that
     *                                 are to be saved.
     * @param packetsToAddParamsList list of the {@link PacketParams} representing the new {@link Packet}s that
     *                                 are to be saved.
     * @param packetsToUpdateParamsList list of the {@link PacketParams} representing the updated {@link Packet}s
     *                                  that are to be saved.
     * @param packetId the {@link Packet} id that is to be saved or null if all the {@link Packet}s are to be
     *                 saved.
     * @return the immutable error map. Empty if no errors occurred. Contains special constants for the cases when
     *         either the {@link Compt}s update or {@link Packet} update or {@link Packet} adding caused errors.
     */
    public Map<String, Boolean> saveAllChangesToBase(List<Long> comptIdsToDelete,
                                                     List<Long> packetIdsToDelete,
                                                     List<ComptParams> comptsToUpdateParamsList,
                                                     List<PacketParams> packetsToAddParamsList,
                                                     List<PacketParams> packetsToUpdateParamsList,
                                                     Long packetId) {

        log.info(Optional.ofNullable(packetId).isPresent()
                ? getMessage(PERSIST_SPECIFIC_PACKET, new Object[]{packetId})
                : getMessage(PERSIST_ALL_PACKETS, null));

        Map<String, Boolean> persistErrors = new HashMap<>();

        if (!comptIdsToDelete.isEmpty()) {
            packetAppDao.deleteCompts(comptIdsToDelete);
        }

        if (!Optional.ofNullable(packetId).isPresent() && !packetIdsToDelete.isEmpty()) {
            packetAppDao.deletePackets(packetIdsToDelete);
        }

        if (!comptsToUpdateParamsList.isEmpty()) {
            try {
                packetAppDao.updateCompts(comptsToUpdateParamsList);
            } catch (DatabaseException e) {
                updateComptsExceptionMessage = e.getMessage();
                updateComptsExceptionStackTrace = ExceptionUtils.getStackTrace(e);
                log.error(getMessage(UPDATE_COMPTS_EXCEPTION_REPORT, new Object[]{updateComptsExceptionMessage,
                                                                                  updateComptsExceptionStackTrace}));
                persistErrors.put(UPDATE_COMPTS, true);
            }
        }

        if (!packetsToAddParamsList.isEmpty()) {
            try {
                packetAppDao.addOrUpdatePackets(packetsToAddParamsList, OperationType.ADD);
            } catch (DatabaseException e) {
                addPacketsExceptionMessage = e.getMessage();
                addPacketsExceptionStackTrace = ExceptionUtils.getStackTrace(e);
                log.error(getMessage(ADD_PACKETS_EXCEPTION_REPORT, new Object[]{addPacketsExceptionMessage,
                                                                                addPacketsExceptionStackTrace}));
                persistErrors.put(ADD_PACKETS, true);
            }
        }

        if (!packetsToUpdateParamsList.isEmpty()) {
            try {
                packetAppDao.addOrUpdatePackets(packetsToUpdateParamsList, OperationType.UPDATE);
            } catch (DatabaseException e) {
                updatePacketsExceptionMessage = e.getMessage();
                updatePacketsExceptionStackTrace = ExceptionUtils.getStackTrace(e);
                log.error(getMessage(UPDATE_PACKETS_EXCEPTION_REPORT, new Object[]{updatePacketsExceptionMessage,
                        updatePacketsExceptionStackTrace}));
                persistErrors.put(UPDATE_PACKETS, true);
            }
        }

        return Collections.unmodifiableMap(persistErrors);
    }
}
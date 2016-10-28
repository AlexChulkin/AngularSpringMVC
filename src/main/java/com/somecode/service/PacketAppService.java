package com.somecode.service;

import com.somecode.dao.PacketAppDao;
import com.somecode.domain.*;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.somecode.utils.Utils.getMessage;

@Service("comptService")
@Log4j
public class PacketAppService {
    private static final String LOAD_DATA_FOR_ALL_PACKETS = "packetAppService.loadDataForAllPackets";
    private static final String LOAD_DATA_FOR_GIVEN_PACKET = "packetAppService.loadDataForGivenPacket";
    private static final String PERSIST_ALL_PACKETS = "packetAppService.persistDataForAllPackets";
    private static final String PERSIST_GIVEN_PACKET = "packetAppService.persistDataForGivenPacket";
    private static final String UPDATE_COMPTS_EXCEPTION_REPORT = "packetAppService.updateCompts.exceptionReport";
    private static final String UPDATE_PACKETS_EXCEPTION_REPORT = "packetAppService.updatePackets.exceptionReport";
    private static final String ADD_PACKETS_EXCEPTION_REPORT = "packetAppService.addPackets.exceptionReport";

    private static final String UPDATE_COMPTS = "UPDATE_COMPTS";
    private static final String UPDATE_PACKETS = "UPDATE_PACKETS";
    private static final String ADD_PACKETS = "ADD_PACKETS";

    private String updateComptsExceptionStackTrace;
    private String updatePacketsExceptionStackTrace;
    private String addPacketsExceptionStackTrace;
    private String updateComptsExceptionMessage;
    private String updatePacketsExceptionMessage;
    private String addPacketsExceptionMessage;


    @Autowired
    private PacketAppDao packetAppDao;

    public Role getUserRole(String username, String password) {
        return packetAppDao.getUserRole(username, password);
    }

    public Data loadData(Long packetId) {
        log.info(packetId == null
                ? getMessage(LOAD_DATA_FOR_ALL_PACKETS, null)
                : getMessage(LOAD_DATA_FOR_GIVEN_PACKET, new Object[]{packetId}));

        Data result = new Data();
        List<State> states;

        try {
            states = packetAppDao.loadAllStates();
        } catch (DatabaseException e) {
            states = Collections.emptyList();
        }

        result.setStates(states);

        List<ComboData> comboDatas;

        try {
            comboDatas = packetAppDao.loadAllComboData();
        } catch (DatabaseException e) {
            comboDatas = Collections.emptyList();
        }

        result.setComboData(comboDatas);

        result.setPackets(packetAppDao.loadPackets(packetId)).setCompts(packetAppDao.loadCompts(packetId));

        if (!result.getCompts().isEmpty() && !result.getStates().isEmpty() && !result.getComboData().isEmpty()) {
            result.setComptSupplInfo(packetAppDao.loadComptsSupplInfo(packetId));
            return result;
        }

        return result.setComptSupplInfo(Collections.emptyList());
    }

    public Map<String, Boolean> saveAllChangesToBase(List<Long> comptIdsToDelete,
                                                     List<Long> packetIdsToDelete,
                                                     List<ComptParams> comptsToUpdateParamsList,
                                                     List<PacketParams> packetsToAddParamsList,
                                                     List<PacketParams> packetsToUpdateParamsList,
                                                     Long packetId) {

        log.info(packetId != null
                ? getMessage(PERSIST_GIVEN_PACKET, new Object[]{packetId})
                : getMessage(PERSIST_ALL_PACKETS, null));

        Map<String, Boolean> persistErrors = new HashMap<>();

        if (!comptIdsToDelete.isEmpty()) {
            packetAppDao.deleteCompts(comptIdsToDelete);
        }

        if (packetId == null && !packetIdsToDelete.isEmpty()) {
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

        return persistErrors;
    }
}
package com.somecode.service;

import com.somecode.dao.PacketAppDao;
import com.somecode.domain.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.somecode.helper.Helper.getMessage;

@Service("comptService")
@Log4j
public class PacketAppService {
    private static final String LOAD_DATA_FOR_ALL_PACKETS = "packetAppService.loadDataForAllPackets";
    private static final String LOAD_DATA_FOR_GIVEN_PACKET = "packetAppService.loadDataForGivenPacket";
    private static final String PERSIST_ALL_PACKETS = "packetAppService.persistDataForAllPackets";
    private static final String PERSIST_GIVEN_PACKET = "packetAppService.persistDataForGivenPacket";
    private static final String EXCEPTION_MESSAGE = "packetAppService.exceptionMessage";

    private static final String UPDATE_COMPTS = "UPDATE_COMPTS";
    private static final String UPDATE_PACKETS = "UPDATE_PACKETS";
    private static final String ADD_PACKETS = "ADD_PACKETS";

    @Autowired
    private PacketAppDao packetAppDao;

    public Role getUserRole(String username, String password) {
        return packetAppDao.getUserRole(username, password);
    }

    public Data loadData(Long packetId) {
        log.debug(packetId == null
                ? getMessage(LOAD_DATA_FOR_ALL_PACKETS, null)
                : getMessage(LOAD_DATA_FOR_GIVEN_PACKET, new Object[]{packetId}));

        Data result = new Data();
        try {
            result.setStates(packetAppDao.loadAllStates());
        } catch (DatabaseException e) {
            result.setStates(Collections.EMPTY_LIST);
        }
        try {
            result.setComboData(packetAppDao.loadAllComboData());
        } catch (DatabaseException e) {
            result.setComboData(Collections.EMPTY_LIST);
        }

        result.setPackets(packetAppDao.loadPackets(packetId))
                .setCompts(packetAppDao.loadCompts(packetId));

        if (!result.getCompts().isEmpty() && !result.getStates().isEmpty() && !result.getComboData().isEmpty()) {
            result.setComptSupplInfo(packetAppDao.loadComptsSupplInfo(packetId));
            return result;
        }

        return result.setComptSupplInfo(Collections.EMPTY_LIST);
    }

    public Map<String, Boolean> saveAllChangesToBase(List<Long> comptIdsToDelete,
                                                     List<Long> packetIdsToDelete,
                                                     List<ComptParams> comptsToUpdateParamsList,
                                                     List<PacketParams> packetsToAddParamsList,
                                                     List<PacketParams> packetsToUpdateParamsList,
                                                     Long packetId) {

        log.debug(packetId != null
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
                log.error(getMessage(EXCEPTION_MESSAGE, new Object[]{e.getMessage(), e.getStackTrace()}));
                persistErrors.put(UPDATE_COMPTS, true);
            }
        }
        persistErrors.putIfAbsent(UPDATE_COMPTS, false);

        if (!packetsToAddParamsList.isEmpty()) {
            try {
                packetAppDao.addOrUpdatePackets(packetsToAddParamsList, OperationType.ADD);
            } catch (DatabaseException e) {
                log.error(getMessage(EXCEPTION_MESSAGE, new Object[]{e.getMessage(), e.getStackTrace()}));
                persistErrors.put(ADD_PACKETS, true);
            }
        }
        persistErrors.putIfAbsent(ADD_PACKETS, false);

        if (!packetsToUpdateParamsList.isEmpty()) {
            try {
                packetAppDao.addOrUpdatePackets(packetsToUpdateParamsList, OperationType.UPDATE);
            } catch (DatabaseException e) {
                log.error(getMessage(EXCEPTION_MESSAGE, new Object[]{e.getMessage(), e.getStackTrace()}));
                persistErrors.put(UPDATE_PACKETS, true);
            }
        }
        persistErrors.put(UPDATE_PACKETS, false);

        return persistErrors;
    }
}
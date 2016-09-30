package com.somecode.service;

import com.somecode.dao.PacketAppDao;
import com.somecode.domain.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static com.somecode.helper.Helper.getMessage;

@Service("comptService")
@Log4j
public class PacketAppService {
    private static final String LOAD_DATA_FOR_ALL_PACKETS = "packetAppService.loadDataForAllPackets";
    private static final String LOAD_DATA_FOR_GIVEN_PACKET = "packetAppService.loadDataForGivenPacket";
    private static final String PERSIST_ALL_PACKETS = "packetAppService.persistDataForAllPackets";
    private static final String PERSIST_GIVEN_PACKET = "packetAppService.persistDataForGivenPacket";
    private static final String EXCEPTION_MESSAGE = "packetAppService.exceptionMessage";

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

        if (!result.getCompts().isEmpty()
                && !result.getStates().isEmpty()
                && !result.getComboData().isEmpty()) {
            result.setComptSupplInfo(packetAppDao.loadComptsSupplInfo(packetId));
            return result;
        }

        return result.setComptSupplInfo(Collections.EMPTY_LIST);
    }

    public EnumSet<PersistError> saveAllChangesToBase(List<Long> comptIdsToDelete,
                                                      List<Long> packetIdsToDelete,
                                                      List<ComptParams> comptsToUpdateParamsList,
                                                      List<PacketParams> packetsToAddParamsList,
                                                      List<PacketParams> packetsToUpdateParamsList,
                                                      Long packetId) {

        log.debug(packetId != null
                ? getMessage(PERSIST_GIVEN_PACKET, new Object[]{packetId})
                : getMessage(PERSIST_ALL_PACKETS, null));

        EnumSet<PersistError> persistErrors = EnumSet.allOf(PersistError.class);

        if (!comptIdsToDelete.isEmpty()) {
            packetAppDao.deleteCompts(comptIdsToDelete);
        }
        persistErrors.remove(PersistError.DELETE_COMPTS);

        if (packetId == null && !packetIdsToDelete.isEmpty()) {
            packetAppDao.deletePackets(packetIdsToDelete);
        }
        persistErrors.remove(PersistError.DELETE_PACKETS);

        if (!comptsToUpdateParamsList.isEmpty()) {
            try {
                packetAppDao.updateCompts(comptsToUpdateParamsList);
                persistErrors.remove(PersistError.UPDATE_COMPTS);
            } catch (DatabaseException e) {
                log.error(getMessage(EXCEPTION_MESSAGE, new Object[]{e.getMessage(), e.getStackTrace()}));
            }
        } else {
            persistErrors.remove(PersistError.UPDATE_COMPTS);
        }

        if (!packetsToAddParamsList.isEmpty()) {
            try {
                packetAppDao.addOrUpdatePackets(packetsToAddParamsList, OperationType.ADD);
                persistErrors.remove(PersistError.ADD_PACKETS);
            } catch (DatabaseException e) {
                log.error(getMessage(EXCEPTION_MESSAGE, new Object[]{e.getMessage(), e.getStackTrace()}));
            }
        } else {
            persistErrors.remove(PersistError.ADD_PACKETS);
        }

        if (!packetsToUpdateParamsList.isEmpty()) {
            try {
                packetAppDao.addOrUpdatePackets(packetsToUpdateParamsList, OperationType.UPDATE);
                persistErrors.remove(PersistError.UPDATE_PACKETS);
            } catch (DatabaseException e) {
                log.error(getMessage(EXCEPTION_MESSAGE, new Object[]{e.getMessage(), e.getStackTrace()}));
            }
        } else {
            persistErrors.remove(PersistError.UPDATE_PACKETS);
        }

        return persistErrors;
    }
}
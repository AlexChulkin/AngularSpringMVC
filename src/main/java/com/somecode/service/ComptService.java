package com.somecode.service;

import com.somecode.dao.ComptDao;
import com.somecode.domain.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;


@Service("comptService")
public class ComptService {
    private static final Logger LOGGER = Logger.getLogger(ComptService.class);

    @Autowired
    private ComptDao comptDao;

    public Data loadData(Long packetId) {
        LOGGER.info(packetId == null
                ? "ComptService. Load All Data."
                : "ComptService. Load data for packet#" + packetId);
        Data result = new Data();
        try {
            result.setStates(comptDao.loadAllStates());
        } catch (DatabaseException e) {
            result.setStates(Collections.EMPTY_LIST);
        }
        try {
            result.setComboData(comptDao.loadAllComboData());
        } catch (DatabaseException e) {
            result.setComboData(Collections.EMPTY_LIST);
        }

        result.setPackets(comptDao.loadPackets(packetId))
                .setCompts(comptDao.loadCompts(packetId));

        if (!result.getCompts().isEmpty()
                && !result.getStates().isEmpty()
                && !result.getComboData().isEmpty()) {
            result.setComptSupplInfo(comptDao.loadComptsSupplInfo(packetId));
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

        LOGGER.info(packetId != null ? "ComptService. Persist All Data." : "ComptService. Persist the packet# " +
                packetId);

        EnumSet<PersistError> persistErrors = EnumSet.allOf(PersistError.class);

        comptDao.deleteCompts(comptIdsToDelete);
        persistErrors.remove(PersistError.DELETE_COMPTS);

        if (packetId == null) {
            comptDao.deletePackets(packetIdsToDelete);
        }
        persistErrors.remove(PersistError.DELETE_PACKETS);

        try {
            comptDao.updateCompts(comptsToUpdateParamsList);
            persistErrors.remove(PersistError.UPDATE_COMPTS);
        } catch (DatabaseException e) {
            LOGGER.error("Exception: " + e.getMessage() + "\nStacktrace: " + e.getStackTrace());
        }

        try {
            comptDao.addOrUpdatePackets(packetsToAddParamsList, OperationType.ADD);
            persistErrors.remove(PersistError.ADD_PACKETS);
        } catch (DatabaseException e) {
            LOGGER.error("Exception: " + e.getMessage() + "\nStacktrace: " + e.getStackTrace());
        }

        try {
            comptDao.addOrUpdatePackets(packetsToUpdateParamsList, OperationType.UPDATE);
            persistErrors.remove(PersistError.UPDATE_PACKETS);
        } catch (DatabaseException e) {
            LOGGER.error("Exception: " + e.getMessage() + "\nStacktrace: " + e.getStackTrace());
        }

        return persistErrors;
    }
}
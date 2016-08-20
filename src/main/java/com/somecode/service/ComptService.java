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

    public List<Long> deleteCompts(List<Long> idsToDelete) {
        return
    }


    public Data getAllData() {
        Data result = new Data();
        try {
            result.setStates(comptDao.getAllStates());
        } catch (DatabaseException e) {
            result.setStates(Collections.EMPTY_LIST);
        }
        try {
            result.setComboData(comptDao.getAllComboData());
        } catch (DatabaseException e) {
            result.setComboData(Collections.EMPTY_LIST);
        }

        result.setPackets(comptDao.getAllPackets())
                .setCompts(comptDao.getAllCompts());

        if (!result.getCompts().isEmpty()
                && !result.getStates().isEmpty()
                && !result.getComboData().isEmpty()) {
            return result.setComptSupplInfo(comptDao.getAllComptsSupplInfo());
        }

        return result.setComptSupplInfo(Collections.EMPTY_LIST);
    }

    public List<ComptSupplInfo> getComptsSupplInfoByPacketId(long packetId) {
        return comptDao.getComptsSupplInfoByPacketId(packetId);
    }

    public EnumSet<PersistError> persistToBase(List<Long> comptIdsToDelete,
                                               List<Long> packetIdsToDelete,
                                               List<ComptParams> updateComptsParamsList,
                                               List<PacketParams> createPacketParamsList,
                                               List<PacketParams> updatePacketParamsList) {

        EnumSet<PersistError> persistErrors = EnumSet.noneOf(PersistError.class);

        comptDao.deleteCompts(comptIdsToDelete);
        comptDao.deletePackets(packetIdsToDelete);

        try {
            comptDao.updateCompts(updateComptsParamsList);
        } catch (DatabaseException e) {
            LOGGER.error("Exception: " + e.getMessage() + "\nStacktrace: " + e.getStackTrace());
            persistErrors.add(PersistError.UPDATE_COMPTS);
        }

        try {
            comptDao.saveOrUpdatePackets(createPacketParamsList, OperationType.ADD);
        } catch (DatabaseException e) {
            LOGGER.error("Exception: " + e.getMessage() + "\nStacktrace: " + e.getStackTrace());
            persistErrors.add(PersistError.ADD_PACKETS);
        }

        try {
            comptDao.saveOrUpdatePackets(updatePacketParamsList, OperationType.UPDATE);
        } catch (DatabaseException e) {
            LOGGER.error("Exception: " + e.getMessage() + "\nStacktrace: " + e.getStackTrace());
            persistErrors.add(PersistError.UPDATE_PACKETS);
        }

        return persistErrors;
    }
}
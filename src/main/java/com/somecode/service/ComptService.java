package com.somecode.service;

import com.somecode.dao.ComptDao;
import com.somecode.domain.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service("comptService")
public class ComptService {
    private static final Logger LOGGER = Logger.getLogger(ComptService.class);

    @Autowired
    private ComptDao comptDao;

    public List<Long> deleteCompts(List<Long> idsToDelete) {
        return comptDao.deleteCompts(idsToDelete);
    }

    public List<Long> deletePackets(List<Long> packetIdsToDelete) {
        return comptDao.deletePackets(packetIdsToDelete);
    }

    public List<ComptInfo> getComptsByPacketId(long packetId) {
        return comptDao.getComptsByPacketId(packetId);
    }

    public Data getAllData() {
        Data result = new Data();
        result.setPackets(comptDao.getAllPackets())
                .setCompts(comptDao.getAllCompts())
                .setStates(comptDao.getAllStates())
                .setComboData(comptDao.getAllComboData());
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

    public Set<String> saveOrUpdatePackets(List<ComptParams> updateComptsParamsList,
                                           List<PacketParams> createPacketParamsList,
                                           List<PacketParams> updatePacketParamsList) {
        Set<String> result = new HashSet<>();
        try {
            comptDao.updateCompts(updateComptsParamsList);
        } catch (DatabaseException e) {
            LOGGER.error("Exception: " + e.getMessage() + "\nStacktrace: " + e.getStackTrace());
            result.add("UpdateCompts");
        }
        comptDao.saveOrUpdatePackets(createPacketParamsList, updatePacketParamsList);
    }
}
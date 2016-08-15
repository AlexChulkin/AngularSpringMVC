package com.somecode.service;

import com.somecode.dao.ComptDao;
import com.somecode.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("comptService")
public class ComptService {
    @Autowired
    private ComptDao comptDao;

    public List<Long> addCompts(long packetId, List<ComptParams> comptParamsList) {
        return comptDao.addCompts(packetId, comptParamsList);
    }

    public List<Long> addPackets(List<PacketParams> packetParamsList) {
        return comptDao.addPackets(packetParamsList);
    }

    public List<Long> deleteCompts(List<Long> idsToDelete) {
        return comptDao.deleteCompts(idsToDelete);
    }

    public List<Long> deletePackets(List<Long> packetIdsToDelete) {
        return comptDao.deletePackets(packetIdsToDelete);
    }

    public List<Long> updateCompts(List<ComptParams> comptParamsList) {
        return comptDao.updateCompts(comptParamsList);
    }

    public List<Long> updatePackets(List<PacketParams> updatedPackets) {
        return comptDao.updatePackets(updatedPackets);
    }

    public List<ComboData> getAllComboData() {
        return comptDao.getAllComboData();
    }

    public List<ComptInfo> getComptsByPacketId(long packetId) {
        return comptDao.getComptsByPacketId(packetId);
    }

    public List<ComptInfo> getAllCompts() {
        return comptDao.getAllCompts();
    }

    public List<ComptSupplInfo> getComptsSupplInfoByPacketId(long packetId) {
        return comptDao.getComptsSupplInfoByPacketId(packetId);
    }

    public List<ComptSupplInfo> getAllComptsSupplInfo() {
        return comptDao.getAllComptsSupplInfo();
    }

    public List<State> getAllStates() {
        return comptDao.getAllStates();
    }

    public List<PacketInfo> getAllPackets() {
        return comptDao.getAllPackets();
    }

    public Long getPacketStateId(long packetId) {
        return comptDao.getPacketStateId(packetId);
    }
}
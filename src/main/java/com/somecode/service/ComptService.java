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

    public List<Long> addCompts(long packetId, List<ComptsParams> comptsParamsList) {
        return comptDao.addCompts(packetId, comptsParamsList);
    }

    public List<Long> removeCompts(List<Long> idsToRemove) {
        return comptDao.removeCompts(idsToRemove);
    }

    public List<Long> updateCompts(List<ComptsParams> comptsParamsList) {
        return comptDao.updateCompts(comptsParamsList);
    }

    public Long updatePacketState(long packetId, long newStateId) {
        return comptDao.updatePacketState(packetId, newStateId);
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
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

    public List<ComboData> getDefaultComboData() {
        return comptDao.getDefaultComboData();
    }

    public List<ComptInfo> getCompts(long packetId) {
        return comptDao.getCompts(packetId);
    }

    public  List<ComptSupplInfo> getComptsSupplInfo(long packetId) {
        return comptDao.getComptsSupplInfo(packetId);
    }

    public  List<State> getStates() {
        return comptDao.getStates();
    }

    public Long getPacketStateId(long packetId) {
        return comptDao.getPacketStateId(packetId);
    }
}
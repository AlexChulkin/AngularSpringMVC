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

    public void addCompts(long packetId, List<ComptsParams> comptsParamsList) {
        comptDao.addCompts(packetId, comptsParamsList);
    }

    public void removeCompts(List<Long> idsToRemove) {
        comptDao.removeCompts(idsToRemove);
    }

    public void updateCompts(List<ComptsParams> comptsParamsList) {
        comptDao.updateCompts(comptsParamsList);
    }

    public void updatePacketsState(long packetId, long newStateId) {
        comptDao.updatePacketsState(packetId,newStateId);
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

    public  long getPacketState(long packetId) {
        return comptDao.getPacket(packetId).getState().getId();
    }
}

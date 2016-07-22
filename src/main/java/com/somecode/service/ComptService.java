package com.somecode.service;

import com.somecode.dao.ComptDao;
import com.somecode.domain.ComboData;
import com.somecode.domain.ComptInfo;
import com.somecode.domain.ComptSupplInfo;
import com.somecode.domain.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("comptService")
public class ComptService {
    @Autowired
    private ComptDao comptDao;

    public void addCompt(String label, long packetId, String[] defaultVals) {
        comptDao.addCompt(label, packetId, defaultVals);
    }

    public void removeCompts(List<Long> idsToRemove) {
        comptDao.removeCompts(idsToRemove);
    }

    public void updateCompt(long comptId, String[] defaultVals) {
        comptDao.updateCompt(comptId,defaultVals);
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

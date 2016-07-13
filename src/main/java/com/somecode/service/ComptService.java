package com.somecode.service;

import com.somecode.dao.ComptDao;
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
    public void removeCompts(Long[] idsToRemove) {
        comptDao.removeCompts(idsToRemove);
    }
    public void updateCompt(long comptId, String[] defaultVals) {
        comptDao.updateCompt(comptId,defaultVals);
    }
    public List<Object[]> getComponents(long packetId) {
        return comptDao.getComponents(packetId);
    }

    
    public  List<Object[]> getStaticData(long packetId) {
        return comptDao.getStaticData(packetId);
    }

    
    public  List<State> getStates() {
        return comptDao.getStates();
    }

    
    public  long getPacketState(long packetId) {
        return comptDao.getPacket(packetId).getState().getId();
    }



}

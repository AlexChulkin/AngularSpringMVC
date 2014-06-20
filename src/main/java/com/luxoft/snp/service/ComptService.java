package com.luxoft.snp.service;

import com.luxoft.snp.dao.ComptDao;
import com.luxoft.snp.domain.Compt;
import com.luxoft.snp.domain.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("comptService")
public class ComptService {

    @Autowired
    private ComptDao comptDao;


    public void addCompt(String label, int packetId, String[] defaultVals) {
        comptDao.addCompt(label, packetId, defaultVals);
    }
    public void removeCompts(String idsToRemove) {
        comptDao.removeCompts(idsToRemove);
    }
    public void updateCompt(int comptId, String[] defaultVals) {
        comptDao.updateCompt(comptId,defaultVals);
    }
    public List<Compt> getComponents(int packetId) {
        return comptDao.getComponents(packetId);
    }

    
    public  List<Object[]> getStaticData(int packetId) {
        return comptDao.getStaticData(packetId);
    }

    
    public  List<State> getStates() {
        return comptDao.getStates();
    }

    
    public  Integer getPacketState(int packetId) {
        return comptDao.getPacket(packetId).getState().getId();
    }



}

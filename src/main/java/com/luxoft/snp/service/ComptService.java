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

    public  void insertData() {
        comptDao.insertData();
    }

    public  void removeData() {
        comptDao.removeData();
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
        return comptDao.getPacketState(packetId);
    }



}

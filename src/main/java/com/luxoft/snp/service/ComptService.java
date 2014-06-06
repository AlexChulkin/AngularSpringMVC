package com.luxoft.snp.service;

import com.luxoft.snp.dao.ComptDao;
import com.luxoft.snp.dao.ComptDaoImpl;
import com.luxoft.snp.domain.Compt;
import com.luxoft.snp.domain.State;

import com.luxoft.snp.domain.PseudoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("comptService")
public class ComptService {

    @Autowired
    private ComptDao comptDao;


    @Transactional(readOnly=true)
    public List<Compt> getComponents(int packageId) {
        return comptDao.getComponents(packageId);
    }

    @Transactional(readOnly=true)
    public  List<PseudoData> getStaticData(int packageId) {
        return comptDao.getStaticData(packageId);
    }

    @Transactional(readOnly=true)
    public  List<State> getStates() {
        return comptDao.getStates();
    }

    @Transactional(readOnly=true)
    public  Integer getPacketState(int packageId) {
        return comptDao.getPacketState(packageId);
    }


}

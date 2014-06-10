package com.luxoft.snp.service;

import com.luxoft.snp.dao.ComptDao;
import com.luxoft.snp.domain.Compt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("comptService")
public class ComptService {

    @Autowired
    private ComptDao comptDao;


    public void save(Compt compt){
        comptDao.sameCompt(compt);
    }

    public List<Compt> getAll(){
        return comptDao.getAllCompts();
    }


}

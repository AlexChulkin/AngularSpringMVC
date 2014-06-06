package com.luxoft.snp.service;

import com.luxoft.snp.dao.ComptDao;
import com.luxoft.snp.domain.Compt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ComptService {

    @Autowired
    private ComptDao comptDao;



    public void saveCompt() {
      comptDao.sameCompt();
    }


    public List<Compt> getAllCompts(){
       return comptDao.getAllCompts();
    }



}

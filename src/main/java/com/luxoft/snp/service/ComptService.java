package com.luxoft.snp.service;

import com.luxoft.snp.dao.ComptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ComptService {

    @Autowired
    private ComptDao comptDao;



    public void saveCompt() {
      comptDao.sameCompt();
    }



}

package com.luxoft.snp.dao;

import com.luxoft.snp.domain.Compt;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class ComptDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void sameCompt(){

        Compt compt = new Compt();
        compt.setLabel("test lable ");
        System.out.println("samveComopt");

        em.persist(compt);
    }



}

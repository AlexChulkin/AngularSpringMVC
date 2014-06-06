package com.luxoft.snp.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Vharutyunyan on 06.06.2014.
 */
@Repository
public class TestDao {

    @PersistenceContext
    private EntityManager em;


    public void test(){


    }
}

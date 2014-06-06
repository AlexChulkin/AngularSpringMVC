package com.luxoft.snp.dao;

import com.luxoft.snp.domain.Compt;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

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

    public List<Compt> getAllCompts(){
        TypedQuery<Compt> query = em.createQuery("select c from Compt c", Compt.class);
        return query.getResultList();

    }



}

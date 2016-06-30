package com.somecode.dao;

import com.somecode.domain.StaticData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by alexc_000 on 2016-06-24.
 */
@Repository
public interface StaticDataRepository extends CrudRepository<StaticData,Long> {
    List<StaticData> findAllByOrderByIdAsc();
}

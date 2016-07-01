package com.somecode.dao;

import com.somecode.domain.DataCompt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by alexc_000 on 2016-06-30.
 */
public interface DataComptRepository extends CrudRepository<DataCompt, Long> {
    List<DataCompt> findByCompt_id(long comptId);
    List<DataCompt> removeByCompt_IdIn(List<Long> comptIdsList);
}

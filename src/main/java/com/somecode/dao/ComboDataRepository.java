package com.somecode.dao;

import com.somecode.domain.ComboData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by alexc_000 on 2016-06-24.
 */
@Repository
public interface ComboDataRepository extends CrudRepository<ComboData, Long> {
    List<ComboData> findAllByOrderByIdAsc();
}

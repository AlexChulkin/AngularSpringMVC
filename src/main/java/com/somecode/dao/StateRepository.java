package com.somecode.dao;

import com.somecode.domain.State;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by alexc_000 on 2016-06-30.
 */
public interface StateRepository extends CrudRepository<State,Long> {
}

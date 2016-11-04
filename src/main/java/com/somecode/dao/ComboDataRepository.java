/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.dao;

import com.somecode.domain.ComboData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The CrudRepository implementation for the {@link ComboData} entity
 */
@Repository
public interface ComboDataRepository extends CrudRepository<ComboData, Long> {
}

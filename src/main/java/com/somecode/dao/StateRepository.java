/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.dao;

import com.somecode.domain.State;
import org.springframework.data.repository.CrudRepository;
/**
 * The CrudRepository implementation for the {@link State} entity.
 * @version 1.0
 */
public interface StateRepository extends CrudRepository<State, Long> {
}

/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.dao;

import com.somecode.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * The CrudRepository implementation for the User.java class
 */
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Finds the user by username.
     *
     * @param username the username.
     * @return the list of users with one entry in case of successful authorization or empty list, otherwise.
     */
    List<User> findByUsername(String username);
}

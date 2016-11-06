/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

/**
 * The self-expaining EntityType interface used in the DAO.
 * @version 1.0
 */
public interface EntityType extends EntityProtoType {
    Integer getVersion();
}


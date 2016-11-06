/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

/**
 * The entity class with the id setter returning the entity instance itself.
 * This was used for the unit testing.
 * @version 1.0
 */

public interface SelfSettingEntityPrototype {
    Long getId();
    SelfSettingEntityPrototype setId(Long id);
}

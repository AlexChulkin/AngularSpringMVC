/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

/**
 * The entity class with the id setter returning the entity instance itself.
 * It's used for the testing.
 */

public interface SelfSettingEntityPrototype {
    Long getId();
    SelfSettingEntityPrototype setId(Long id);
}

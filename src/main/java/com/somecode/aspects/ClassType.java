
/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.aspects;

/*
 * Enum showing what module is chosen to implement AOP or test: service, restful controller or dao.
 * @version 1.0
 */
public enum ClassType {
    /**
     * Service constant
     */
    SERVICE,
    /**
     * Restful controller constant
     */
    CONTROLLER,
    /**
     * DAO constant
     */
    DAO
}

/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

import com.somecode.utils.Utils;

/**
 * The custom database exception used to indicate the problems with the database.
 * @version 1.0
 */
public class DatabaseException extends Exception {
    private static final String DATABASE_EXCEPTION_MESSAGE = "database.exception.message";

    /**
     * Returns the message containing the info about the exception. Used for logging.
     * The pattern is the following:
     * Database exception occurred.
     */
    public String getMessage() {
        return Utils.getMessage(DATABASE_EXCEPTION_MESSAGE, null);
    }
}

/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

import com.somecode.utils.Utils;

/**
 * The custom database exception used to indicate the problems with the database.
 */
public class DatabaseException extends Exception {
    private static final String DATABASE_EXCEPTION_MESSAGE = "database.exception.message";

    /**
     * Returns the message containing the info about the exception. Used for logging.
     *
     * @return the message containing the info about the exception.
     */
    public String getMessage() {
        return Utils.getMessage(DATABASE_EXCEPTION_MESSAGE, null);
    }
}

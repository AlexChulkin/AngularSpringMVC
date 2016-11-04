/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

import com.somecode.utils.Utils;

/**
 * The custom database exception used to indicate the problems with the empty database tables.
 */
public class EmptyDBTableException extends Exception {
    private static final String EMPTY_DB_TABLE_REPORT = "emptyDbTable.exception.report";

    /**
     * Returns the message containing the info about the exception. Used for logging.
     *
     * @return the message containing the info about the exception.
     */
    @Override
    public String getMessage() {
        return Utils.getMessage(EMPTY_DB_TABLE_REPORT, null);
    }
}

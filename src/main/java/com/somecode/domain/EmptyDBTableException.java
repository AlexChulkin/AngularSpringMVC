/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

import com.somecode.utils.Utils;

/**
 * The custom database exception used to indicate the problems with the empty database tables.
 * @version 1.0
 */
public class EmptyDBTableException extends Exception {
    private static final String EMPTY_DB_TABLE_REPORT = "emptyDbTable.exception.report";

    /**
     * Returns the message containing the info about the exception. Used for logging.
     * The pattern is the following:
     * This necessary DB table is empty. DB save/update operation is therefore forbidden.
     */
    @Override
    public String getMessage() {
        return Utils.getMessage(EMPTY_DB_TABLE_REPORT, null);
    }
}

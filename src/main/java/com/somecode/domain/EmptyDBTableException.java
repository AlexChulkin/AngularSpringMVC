package com.somecode.domain;

import com.somecode.utils.Utils;

/**
 * Created by alexc_000 on 2016-08-19.
 */
public class EmptyDBTableException extends Exception {
    private static final String EMPTY_DB_TABLE_REPORT = "emptyDbTable.exception.report";
    public String getMessage() {
        return Utils.getMessage(EMPTY_DB_TABLE_REPORT, null);
    }
}

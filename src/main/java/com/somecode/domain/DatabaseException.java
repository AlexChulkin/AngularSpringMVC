package com.somecode.domain;

import com.somecode.helper.Helper;
/**
 * Created by alexc_000 on 2016-08-19.
 */
public class DatabaseException extends Exception {
    private static final String DATABASE_EXCEPTION_MESSAGE = "database.exception.message";
    public String getMessage() {
        return Helper.getMessage(DATABASE_EXCEPTION_MESSAGE, null);
    }
}

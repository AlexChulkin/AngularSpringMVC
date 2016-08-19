package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-08-19.
 */
public class DatabaseException extends Exception {
    public String getMessage() {
        return "Database exception occurred.";
    }
}

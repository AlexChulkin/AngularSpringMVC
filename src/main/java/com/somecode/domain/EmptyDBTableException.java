package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-08-19.
 */
public class EmptyDBTableException extends Exception {
    public String getMessage() {
        return "This necessary DB table is empty. DB save/update operation" +
                "is therefore forbidden. ";
    }
}

package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-08-19.
 */
public class EmptyStateTableException extends EmptyDBTableException {
    private static final String DELIMITER = ". ";
    public String getMessage() {
        return State.class.getSimpleName() + DELIMITER + super.getMessage();
    }
}

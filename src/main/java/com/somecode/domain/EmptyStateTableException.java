package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-08-19.
 */
public class EmptyStateTableException extends EmptyDBTableException {
    public String getMessage() {
        return State.class.getSimpleName() + super.getMessage();
    }
}

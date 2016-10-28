package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-08-19.
 */
public class EmptyComboDataTableException extends EmptyDBTableException {
    private static final String DELIMITER = ". ";
    public String getMessage() {
        return ComboData.class.getSimpleName() + DELIMITER + super.getMessage();
    }
}

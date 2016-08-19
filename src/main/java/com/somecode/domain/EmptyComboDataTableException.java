package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-08-19.
 */
public class EmptyComboDataTableException extends EmptyDBTableException {
    public String getMessage() {
        return ComboData.class.getSimpleName() + super.getMessage();
    }
}

package com.somecode.domain;

import com.somecode.utils.Utils;

/**
 * Created by alexc_000 on 2016-08-19.
 */
public class EmptyDBTableException extends Exception {
    public String getMessage() {
        return Utils.getMessage("emptyDbTable.exception.message", null);
    }
}

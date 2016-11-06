/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

/**
 * The fairly self-explained class used for passing the data from the front-end to the back-end.
 * @version 1.0
 */
public class RequestObj {
    /**
     * The params for the application data passing
     */
    private DataParams dataParams;
    /**
     * The params for the credentials passing
     */
    private SecurityParams securityParams;

    public SecurityParams getSecurityParams() {
        return securityParams;
    }

    public void setSecurityParams(SecurityParams securityParams) {
        this.securityParams = securityParams;
    }

    public DataParams getDataParams() {
        return dataParams;
    }

    public void setDataParams(DataParams dataParams) {
        this.dataParams = dataParams;
    }
}

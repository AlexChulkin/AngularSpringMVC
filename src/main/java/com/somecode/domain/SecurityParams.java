/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

/**
 * The self-explanatory class for passing of the security data from the back-end to the front-end.
 */
public class SecurityParams {
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

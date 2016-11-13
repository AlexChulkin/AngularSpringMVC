/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.somecode.utils.Utils.getMessage;

/**
 * The self-explanatory {@link User} entity POJO class(for the {@literal USER} table).
 * @version 1.0
 */

@Entity
public class User {
    private static final String ID_COLUMN = "USER_ID";
    private static final String NOT_EMPTY_USERNAME_MESSAGE = "Username can't be empty";
    private static final String SIZE_USERNAME_MESSAGE = "Username length can't exceed 16 symbols";
    private static final String NOT_EMPTY_PASSWORD_MESSAGE = "Password can't be empty";
    private static final String SIZE_PASSWORD_MESSAGE = "Password length can't exceed 16 symbols";
    private static final String NOT_NULL_ROLE_NAME = "Role name can't be null";
    private static final String STRING_VERSION = "user.toString";
    private static final int ID_COLUMN_LENGTH = 2;
    private static final int USERNAME_SIZE = 16;
    private static final int PASSWORD_SIZE = 8;
    private Long id;
    private String username;
    private String password;
    private Role role;
    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_COLUMN, length = ID_COLUMN_LENGTH)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty(message = NOT_EMPTY_USERNAME_MESSAGE)
    @Size(max = USERNAME_SIZE, message = SIZE_USERNAME_MESSAGE)
    @Column
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotEmpty(message = NOT_EMPTY_PASSWORD_MESSAGE)
    @Size(max = PASSWORD_SIZE, message = SIZE_PASSWORD_MESSAGE)
    @Column
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotNull(message = NOT_NULL_ROLE_NAME)
    @Enumerated(EnumType.STRING)
    @Column
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Returns a brief description of this entity.
     * The pattern is the following:
     * User with name: {@link #username} and role: {@link #role}.
     */
    @Override
    public String toString() {
        return getMessage(STRING_VERSION, new Object[]{username, role});
    }
}

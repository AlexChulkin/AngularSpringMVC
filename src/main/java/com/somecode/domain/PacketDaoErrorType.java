/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.domain;

/**
 * The DAO Error Type related with the {@link Packet} operations.
 * Used in the tests.
 * @version 1.0
 */
public enum PacketDaoErrorType {
    /**
     * No Error constant
     */
    NONE,
    /**
     * Constant for the case when there doesn't exist the {@link Packet} id during the single packet update.
     */
    NOT_EXISTING_THE_ONLY_PACKET_ID,
    /**
     * Constant for the case when there doesn't exist the {@link Packet} id during the several packets update.
     */
    NOT_EXISTING_ONE_OF_PACKET_IDS,
    /** Constant for the case of the nullable new {@link State} id for the {@link Packet} update. */
    NULL_NEW_STATE_ID,
    /** Constant for the case of the not-existing new {@link State} id for the {@link Packet} update. */
    NOT_EXISTING_STATE_ID,
    /** Constant for the case of the not different new {@link State} id for the {@link Packet} update. */
    NOT_DIFFERENT_NEW_STATE_ID,
    /** Constant for the case when none of the {@link Compt} ids exist for the {@link Compt} delete. */
    NONE_OF_THE_COMPT_IDS_EXIST,
    /** Constant for the case when some of the {@link Compt} ids exist for the {@link Compt} delete. */
    SOME_OF_THE_COMPT_IDS_EXIST,
    /** Constant for the authentication error case for the user role getting.*/
    AUTHENTICATION_ERROR
}

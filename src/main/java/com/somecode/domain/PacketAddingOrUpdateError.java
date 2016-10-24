package com.somecode.domain;

/**
 * Created by alexc_000 on 2016-10-23.
 */
public enum PacketAddingOrUpdateError {
    NONE,
    NOT_EXISTING_PACKET_ID,
    NULL_NEW_STATE_ID,
    NOT_EXISTING_STATE_ID,
    NOT_DIFFERENT_NEW_STATE_ID
}

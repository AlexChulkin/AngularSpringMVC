/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.dao;

import com.somecode.domain.Packet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
/**
 * The CrudRepository implementation for the {@link Packet} entity
 */
public interface PacketRepository extends CrudRepository<Packet, Long> {
    /**
     * Finds the packets using the ids from the given list.
     *
     * @param idsList the {@link Packet} ids list
     * @return the {@link Packet} entities list.
     */
    List<Packet> findByIdIn(List<Long> idsList);
}

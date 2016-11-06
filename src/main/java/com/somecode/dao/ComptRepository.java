/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.dao;

import com.somecode.domain.Compt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
/**
 * The CrudRepository implementation for the {@link Compt} entity
 * @version 1.0
 */
public interface ComptRepository extends CrudRepository<Compt,Long> {
    /**
     * Finds the compts using the ids from the given list.
     *
     * @param idsList the {@link Compt} ids list
     * @return the list of the {@link Compt} entities.
     */
    List<Compt> findByIdIn(List<Long> idsList);

    /**
     * Finds the compts belonging to the {@link com.somecode.domain.Packet} with the given id.
     *
     * @param packetId the {@link com.somecode.domain.Packet} id
     * @return the list of the {@link com.somecode.domain.Compt} entities.
     */
    List<Compt> findByPacket_Id(Long packetId);
}

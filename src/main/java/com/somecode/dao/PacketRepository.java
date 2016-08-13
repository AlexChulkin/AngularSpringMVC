package com.somecode.dao;

import com.somecode.domain.Packet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by alexc_000 on 2016-08-14.
 */
public interface PacketRepository extends CrudRepository<Packet, Long> {
    List<Packet> findByIdIn(List<Long> idsList);

}

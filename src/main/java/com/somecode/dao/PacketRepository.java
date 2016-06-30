package com.somecode.dao;

import com.somecode.domain.Packet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by alexc_000 on 2016-06-30.
 */
@Repository
public interface PacketRepository extends CrudRepository<Packet,Long> {
}

package com.somecode.dao;

import com.somecode.domain.Compt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by alexc_000 on 2016-06-30.
 */
public interface ComptRepository extends CrudRepository<Compt,Long> {
    List<Compt> findByIdIn(List<Long> idsList);

    List<Compt> findByPacket_Id(Long packetId);
}

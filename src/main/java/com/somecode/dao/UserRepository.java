package com.somecode.dao;

import com.somecode.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by alexc_000 on 2016-09-06.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByUsername(String login);
}

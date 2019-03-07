package org.smither.opwatch.server.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
interface UserDAO extends CrudRepository<User, UUID> {
    List<User> findByUsername(String username);
    List<User> findAll();
}

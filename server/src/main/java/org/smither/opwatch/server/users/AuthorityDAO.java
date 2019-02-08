package org.smither.opwatch.server.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface AuthorityDAO extends CrudRepository<Authority, Long> {
    Optional<Authority> findById(UUID id);

    List<Authority> findByAuthority(String authority);

}

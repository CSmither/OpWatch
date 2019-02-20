package org.smither.opwatch.server.signs;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
interface SignDAO extends CrudRepository<Sign, UUID> {

    List<Sign> findByPlacer(UUID placer);

    List<Sign> findAll();

    List<Sign> findByChecked(boolean checked);

    List<Sign> findByServerAndWorldAndXAndYAndZ(UUID server, String world, int x, int y, int z);

    long countByChecked(boolean checked);
}

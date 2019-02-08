package org.smither.opwatch.server.tokens;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenDAO {

    List<RefreshToken> findByExpiryLessThan(Date expiry);

    Optional<RefreshToken> findById(UUID id);

    List<RefreshToken> findByUser(UUID userId);

    void insert(RefreshToken refreshToken);

    void setUsed(UUID id);

}

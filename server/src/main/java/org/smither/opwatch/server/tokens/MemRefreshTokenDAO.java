package org.smither.opwatch.server.tokens;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MemRefreshTokenDAO implements RefreshTokenDAO {

  private List<RefreshToken> refreshTokens = new ArrayList<>();

  @Override
  public List<RefreshToken> findByExpiryLessThan( Date expiry ) {
    return refreshTokens.stream()
        .filter(refreshToken -> refreshToken.getExpiry().before(expiry))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<RefreshToken> findById( UUID id ) {
    return refreshTokens.stream()
        .filter(refreshToken -> refreshToken.getRefresh().equals(id))
        .findFirst();
  }

  @Override
  public List<RefreshToken> findByUser( UUID userId ) {
    return refreshTokens.stream()
        .filter(refreshToken -> refreshToken.getUser().equals(userId))
        .collect(Collectors.toList());
  }

  @Override
  public void insert( RefreshToken refreshToken ) {
    refreshTokens.add(refreshToken);
  }

  @Override
  public void setUsed( UUID id ) {
    Optional<RefreshToken> token = findById(id);
    token.ifPresent(refreshToken -> refreshToken.setUsed(true));
  }

}

package org.smither.opwatch.server.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smither.opwatch.server.tokens.MemRefreshTokenDAO;
import org.smither.opwatch.server.tokens.RefreshToken;
import org.smither.opwatch.server.tokens.RefreshTokenDAO;
import org.smither.opwatch.server.users.User;
import org.smither.opwatch.server.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private String iss;

    private UserService userService;

    private RefreshTokenDAO refreshTokenDAO;

    private Algorithm algorithm;

    private JWTVerifier jwtVerifier;

    private byte[] secret;

    @Autowired
    public JwtTokenProvider(
            @Value("${jwt.fqdn}") String host, @Value("${jwt.secret}") String secretStr,
            UserService userService, MemRefreshTokenDAO refreshTokenDAO
    ) throws NoSuchAlgorithmException {
        secret = secretStr.getBytes();
        if (secret.length < 6) {
            throw new IllegalArgumentException("JWT_SECRET must be at least 6 characters");
        }
        this.iss = host;
        this.userService = userService;
        this.refreshTokenDAO = refreshTokenDAO;
        setAlgorithm(Algorithm.HMAC512(secret));
    }

    private TokenPair createToken(UUID id) {
        try {
            String access = JWT.create()
                    .withIssuer(iss)
                    .withAudience(iss)
                    .withSubject(id.toString())
                    .withExpiresAt(new Date(new Date().getTime()+900000))
                    .withClaim("type", "access")
                    .sign(algorithm);
            String refresh = JWT.create()
                    .withJWTId(UUID.randomUUID().toString())
                    .withIssuer(iss)
                    .withAudience(iss)
                    .withSubject(id.toString())
                    .withExpiresAt(new Date(new Date().getTime()+1814400000	))
                    .withClaim("type", "refresh")
                    .sign(algorithm);
            DecodedJWT jwt = JWT.decode(refresh);
            refreshTokenDAO.insert(
                    new RefreshToken(UUID.fromString(jwt.getId()), userService.findById(id).get(),
                            jwt.getExpiresAt(), false
                    ));
            return new TokenPair(access, refresh);
        } catch (JWTCreationException exception) {
            log.error(exception.getMessage());
            return null;
        }
    }

    public Authentication getAuthentication(String token) {
        final UUID userId = UUID.fromString(getUserId(token));
        return userService.findById(userId).map(user -> new UsernamePasswordAuthenticationToken(user,
                new UserCredentials(user.getUsername(), ""), user.getAuthorities()
        )).orElseThrow(() -> new UsernameNotFoundException(getUserId(token)));
    }

    public String getUserId(String token) {
        return jwtVerifier.verify(token).getSubject().trim();
    }

    public TokenPair login(String username, String password) {
        List<User> users = userService.findByUsername(username);
        if (users.size() == 1) {
            if (BCrypt.checkpw(password, users.get(0).getPassword())) {
                return createToken(users.get(0).getId());
            } else {
                throw new AuthenticationCredentialsNotFoundException("password");
            }
        } else {
            throw new AuthenticationCredentialsNotFoundException(username);
        }
    }

    public TokenPair refresh(String refresh) {
        if (validateToken(refresh)) {
            TokenPair tokens = createToken(UUID.fromString(getUserId(refresh)));
            Optional<RefreshToken> optOld = refreshTokenDAO.findById(
                    UUID.fromString(JWT.decode(refresh).getId()));
            if (!optOld.isPresent()) {
                return null;
            }
            RefreshToken old = optOld.get();
            refreshTokenDAO.setUsed(old.getRefresh());
            return tokens;
        } else {
            return null;
        }
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(6).trim();
        }
        return bearerToken == null ? null : bearerToken.trim();
    }

    void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        jwtVerifier = JWT.require(algorithm).withIssuer(iss).withAudience(iss).build();
    }

    public boolean validateToken(String token) {
        try {
            DecodedJWT jwt = jwtVerifier.verify(token);
            if (jwt.getClaim("type").asString().equals("refresh")) {
                Optional<RefreshToken> optoken = refreshTokenDAO.findById(UUID.fromString(jwt.getId()));
                if (!optoken.isPresent()) {
                    return false;
                }
                RefreshToken old = optoken.get();
                if (old.isUsed()) {
                    return false;
                }
            }
            return true;
        } catch (JWTVerificationException e) {
            log.error(e.getMessage());
            return false;
        }
    }

}

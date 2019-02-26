package org.smither.opwatch.server.auth;

import org.smither.opwatch.server.exceptions.InputInvalid;
import org.smither.opwatch.server.users.Authority;
import org.smither.opwatch.server.users.UserService;
import org.smither.opwatch.utils.sharedDTO.LoginDTO;
import org.smither.opwatch.utils.sharedDTO.TokenReturnDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class AuthService {

    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    public TokenReturnDTO login(LoginDTO dto) {
        if (!dto.isValidRequest()) {
            throw new InputInvalid("");
        }
        if (dto.getUsername() != null && dto.getPassword() != null) {
            TokenPair returnable = jwtTokenProvider.login(dto.getUsername(),
                    dto.getPassword()
            );
            return TokenReturnDTO.builder().access(returnable.getAccess()).refresh(returnable.getRefresh()).build();
        } else if (dto.getRefresh() != null) {
            TokenPair returnable = jwtTokenProvider.refresh(dto.getRefresh());
            return TokenReturnDTO.builder().access(returnable.getAccess()).refresh(returnable.getRefresh()).build();
        } else {
            throw new InputInvalid("");
        }
    }

    public Authentication getPrinciple(String token) {
        return jwtTokenProvider.getAuthentication(token);
    }
}

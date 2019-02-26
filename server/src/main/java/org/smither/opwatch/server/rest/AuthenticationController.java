package org.smither.opwatch.server.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.smither.opwatch.server.auth.AuthService;
import org.smither.opwatch.utils.sharedDTO.LoginDTO;
import org.smither.opwatch.utils.sharedDTO.TokenReturnDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Authentication",
        description = "Operations pertaining Authentication")
public class AuthenticationController {

    private AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "Login: Fetches a token pair for the user, either pass refresh token or " +
            "basic credentials")
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Never returned but swagger won't let me get rid of it",
                    response = void.class),
            @ApiResponse(code = 201,
                    message = "Successful login/refresh",
                    response = TokenReturnDTO.class),
            @ApiResponse(code = 403, message = "Forbidden - Login failed")
    })
    @PostMapping(value = "/token", produces = "application/json", consumes = "application/json")
    public TokenReturnDTO login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

}

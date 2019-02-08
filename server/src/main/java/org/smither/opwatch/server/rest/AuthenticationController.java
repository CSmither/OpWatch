package org.smither.opwatch.server.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.smither.opwatch.server.auth.JwtTokenProvider;
import org.smither.opwatch.server.auth.TokenPair;
import org.smither.opwatch.server.users.UserService;
import org.smither.opwatch.utils.sharedDTO.LoginDTO;
import org.smither.opwatch.utils.sharedDTO.RegisterDTO;
import org.smither.opwatch.utils.sharedDTO.TokenReturnDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "Authentication",
        description = "Operations pertaining to User details (For " + "authentication see token)")
public class AuthenticationController {

    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    @Autowired
    public AuthenticationController(
            UserService userService, JwtTokenProvider jwtTokenProvider
    ) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
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
    public TokenReturnDTO login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        try {
            if (!loginDTO.isValidRequest()) {
                response.sendError(403, "Field Invalid.");
                return null;
            }
            if (loginDTO.getUsername() != null && loginDTO.getPassword() != null) {
                TokenPair returnable = jwtTokenProvider.login(loginDTO.getUsername(),
                        loginDTO.getPassword()
                );
                if (returnable == null) {
                    response.setStatus(403); //
                    return null;
                }
                response.setStatus(201); //
                return TokenReturnDTO.builder().access(returnable.getAccess()).refresh(returnable.getRefresh()).build();
            } else if (loginDTO.getRefresh() != null) {
                TokenPair returnable = jwtTokenProvider.refresh(loginDTO.getRefresh());
                if (returnable == null) {
                    response.setStatus(403); //
                    return null;
                }
                response.setStatus(201); //
                return TokenReturnDTO.builder().access(returnable.getAccess()).refresh(returnable.getRefresh()).build();
            } else {
                response.sendError(403, "Field Invalid.");
                return null;
            }
        } catch (AuthenticationCredentialsNotFoundException E) {
            response.setStatus(403);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getClass());
            response.setStatus(500);
            return null;
        }
    }

    @ApiOperation(value = "Registers a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Never returned but swagger won't let me get rid of it"),
            @ApiResponse(code = 201, message = "User successfully registered"),
            @ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 401, message = "Invalid username / password"),
            @ApiResponse(code = 409, message = "User exists")
    })
    @PostMapping(value = "/user")
    public void register(@RequestBody RegisterDTO registerDTO, HttpServletResponse response) {
        try {
            userService.createUser(registerDTO.getUsername(), registerDTO.getPassword());
            response.setStatus(201);
        } catch (DataIntegrityViolationException e) {
            response.setStatus(409);
        } catch (IllegalArgumentException e) {
            response.setStatus(400);
        } catch (AuthenticationCredentialsNotFoundException e) {
            response.setStatus(401);
        }
    }

}

package org.smither.opwatch.server.rest;

import io.swagger.annotations.*;
import org.smither.opwatch.server.auth.JwtTokenProvider;
import org.smither.opwatch.server.users.UserService;
import org.smither.opwatch.utils.sharedDTO.CreateAuthDTO;
import org.smither.opwatch.utils.sharedDTO.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Api(value = "Users",
        description = "Operations pertaining to administrating the Users")
public class UserController {

    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    @Autowired
    public UserController(
            UserService userService, JwtTokenProvider jwtTokenProvider
    ) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @ApiOperation(value = "Registers a new user",
            authorizations = {@Authorization(value = "jwtAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Never returned but swagger won't let me get rid of it"),
            @ApiResponse(code = 201, message = "User successfully registered"),
            @ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 401, message = "Invalid username / password"),
            @ApiResponse(code = 409, message = "User exists")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/user")
    public void register(@RequestBody RegisterDTO registerDTO) {
        userService.createUser(registerDTO.getUsername(), registerDTO.getPassword());
    }

    @ApiOperation(value = "Adds an auth to a user",
            authorizations = {@Authorization(value = "jwtAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Never returned but swagger won't let me get rid of it"),
            @ApiResponse(code = 201, message = "Authority successfully added to user"),
            @ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 401, message = "Invalid UserId / AuthId")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/user/{userId}/auth/{authId}")
    public void addAuth(@PathVariable("userId") UUID userId, @PathVariable("authId") UUID authId) {
        userService.addauthority(userId, authId);
    }

    @ApiOperation(value = "Removes an auth from a user",
            authorizations = {@Authorization(value = "jwtAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Never returned but swagger won't let me get rid of it"),
            @ApiResponse(code = 201, message = "Authority successfully added to user"),
            @ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 401, message = "Invalid UserId / AuthId")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/user/{userId}/auth/{authId}")
    public void removeAuth(@PathVariable("userId") UUID userId, @PathVariable("authId") UUID authId) {
        userService.delAuthority(userId, authId);
    }

    @ApiOperation(value = "Registers a new authority",
            authorizations = {@Authorization(value = "jwtAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Never returned but swagger won't let me get rid of it"),
            @ApiResponse(code = 201, message = "Authority successfully registered"),
            @ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 409, message = "Authority exists")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/authority")
    public void createAuthority(@RequestBody CreateAuthDTO createAuthDTO) {
        userService.createAuthority(createAuthDTO.getAuthority());
    }

}

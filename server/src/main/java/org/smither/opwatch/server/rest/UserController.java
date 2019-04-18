package org.smither.opwatch.server.rest;

import io.swagger.annotations.*;
import org.smither.opwatch.server.auth.JwtTokenProvider;
import org.smither.opwatch.server.users.Authority;
import org.smither.opwatch.server.users.UserService;
import org.smither.opwatch.utils.sharedDTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @ApiOperation(value = "Gets all Users",
            authorizations = {@Authorization(value = "jwtAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Never returned but swagger won't let me get rid of it"),
            @ApiResponse(code = 201, message = "User successfully registered"),
            @ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 401, message = "Invalid username / password"),
            @ApiResponse(code = 409, message = "User exists")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/user")
    public List<UserReturnDTO> getUsers() {
        return userService.findAll().stream()
                .map(user -> UserReturnDTO.builder()
                        .id(user.getId())
                        .displayName(user.getDisplayName())
                        .username(user.getUsername())
                        .accountExpiry(user.getAccountExpiry())
                        .credentialsExpiry(user.getCredentialsExpiry())
                        .enabled(user.isEnabled())
                        .locked(user.isLocked())
                        .authorities(user.getAuthorities().stream().collect(Collectors.toMap(Authority::getId, Authority::getAuthority)))
                        .build())
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Gets all Authorities",
            authorizations = {@Authorization(value = "jwtAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Never returned but swagger won't let me get rid of it"),
            @ApiResponse(code = 201, message = "User successfully registered"),
            @ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 401, message = "Invalid username / password"),
            @ApiResponse(code = 409, message = "User exists")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/auth")
    public List<AuthReturnDTO> getAuths() {
        return userService.findAllAuths().stream()
                .map(auth -> AuthReturnDTO.builder()
                        .id(auth.getId())
                        .authority(auth.getAuthority())
                        .build())
                .collect(Collectors.toList());
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/user")
    public void register(@RequestBody RegisterDTO registerDTO) {
        userService.createUser(registerDTO.getUsername(), registerDTO.getPassword());
    }

    @ApiOperation(value = "Deletes an existing user",
            authorizations = {@Authorization(value = "jwtAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Never returned but swagger won't let me get rid of it"),
            @ApiResponse(code = 201, message = "User successfully Deleted"),
            @ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 401, message = "Invalid username / password"),
            @ApiResponse(code = 409, message = "User exists")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/user")
    public void delete(@RequestBody UserDeleteDTO userDeleteDTO) {
        userService.deleteUser(userDeleteDTO.getUsername());
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/authority")
    public void createAuthority(@RequestBody CreateAuthDTO createAuthDTO) {
        userService.createAuthority(createAuthDTO.getAuthority());
    }

}

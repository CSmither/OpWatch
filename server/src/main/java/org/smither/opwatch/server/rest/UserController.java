package org.smither.opwatch.server.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.smither.opwatch.server.auth.JwtTokenProvider;
import org.smither.opwatch.server.users.UserService;
import org.smither.opwatch.utils.sharedDTO.CreateAuthDTO;
import org.smither.opwatch.utils.sharedDTO.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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

    @ApiOperation(value = "Adds an auth to a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Never returned but swagger won't let me get rid of it"),
            @ApiResponse(code = 201, message = "Authority successfully added to user"),
            @ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 401, message = "Invalid UserId / AuthId")
    })
    @PostMapping(value = "/user/{userId}/auth/{authId}")
    public void register(@PathVariable("userId") UUID userId, @PathVariable("authId") UUID authId, HttpServletResponse response) {
        try {
            userService.addauthority(userId, authId);
            response.setStatus(201);
        } catch (DataIntegrityViolationException e) {
            response.setStatus(409);
        } catch (IllegalArgumentException e) {
            response.setStatus(400);
        } catch (AuthenticationCredentialsNotFoundException e) {
            response.setStatus(401);
        }
    }

    @ApiOperation(value = "Registers a new authority")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Never returned but swagger won't let me get rid of it"),
            @ApiResponse(code = 201, message = "Authority successfully registered"),
            @ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 409, message = "Authority exists")
    })
    @PostMapping(value = "/authority")
    public void createAuthority(@RequestBody CreateAuthDTO createAuthDTO, HttpServletResponse response) {
        try {
            userService.createAuthority(createAuthDTO.getAuthority());
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

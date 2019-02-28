package org.smither.opwatch.server.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.smither.opwatch.server.NotificationService;
import org.smither.opwatch.server.signs.Sign;
import org.smither.opwatch.utils.sharedDTO.ErrorDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("problem")
public class ProblemController {

    private NotificationService notificationService;

    public ProblemController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ApiOperation(
            value = "Inform backend system of an error, exception, or problem in any system. Will inform admins.",
            authorizations = {@Authorization(value = "jwtAuth")})
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Returned on successful call", response = Sign.class),
                    @ApiResponse(code = 403, message = "Forbidden - Login failed")
            })
    @PostMapping()
    public void postErrorDto(@RequestBody() ErrorDTO dto) {
        notificationService.sendIRC(dto.getMessage());
    }
}

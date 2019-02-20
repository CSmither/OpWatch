package org.smither.opwatch.server.rest;

import io.swagger.annotations.*;
import org.smither.opwatch.server.signEvents.SignEvent;
import org.smither.opwatch.server.signEvents.SignEventService;
import org.smither.opwatch.server.signs.Sign;
import org.smither.opwatch.server.signs.SignService;
import org.smither.opwatch.utils.sharedDTO.SignEventPostDTO;
import org.smither.opwatch.utils.sharedDTO.SignPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@Api(value = "SignEvents", description = "Operations pertaining to sign events")
public class SignEventController {

  private final SignService signService;

  private final SignEventService signEventService;

  @Autowired
  public SignEventController(SignService signService, SignEventService signEventService) {
    this.signService = signService;
    this.signEventService = signEventService;
  }

  @ApiOperation(
      value = "Gets all signs, or signs that have placed by a specific person",
      authorizations = {@Authorization(value = "jwtAuth")})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Returned on successful call", response = SignEvent.class),
        @ApiResponse(code = 403, message = "Forbidden - Login failed")
      })
  //  @PreAuthorize("hasRole('SERVER')")
  @PostMapping("/signEvent")
  public SignEvent postSignEvent(@RequestBody() SignEventPostDTO signEventPostDTO) {
    return signEventService.createSignEvent(signEventPostDTO);
  }
  @ApiOperation(
          value = "Gets all sign Events or events for specific sign",
          authorizations = {@Authorization(value = "jwtAuth")})
  @ApiResponses(
          value = {
                  @ApiResponse(code = 200, message = "Returned on successful call", response = SignEvent.class),
                  @ApiResponse(code = 403, message = "Forbidden - Login failed")
          })
  @GetMapping(value = "/signEvent")
  public Collection<SignEvent> getSignEvents(@RequestParam(required = false) UUID sign) {
    if (sign != null) {
      return signEventService.getEventsForSign(sign);
    }
    return signEventService.getEvents();
  }

  @ApiOperation(
          value = "Gets specific SignEvent",
          authorizations = {@Authorization(value = "jwtAuth")})
  @ApiResponses(
          value = {
                  @ApiResponse(code = 200, message = "Returned on successful call", response = SignEvent.class),
                  @ApiResponse(code = 403, message = "Forbidden - Login failed")
          })
  @GetMapping("/signEvent/{id}")
  public SignEvent getSignEventWithId(@PathVariable("id") UUID signEventId) {
    return signEventService.getSignEvent(signEventId).orElse(null);
  }

}

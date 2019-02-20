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

import java.util.List;
import java.util.UUID;

@RestController
@Api(value = "Signs", description = "Operations pertaining to Signs")
public class SignController {

  private final SignService signService;

  private final SignEventService signEventService;

  @Autowired
  public SignController(SignService signService, SignEventService signEventService) {
    this.signService = signService;
    this.signEventService = signEventService;
  }

  @ApiOperation(
      value = "Gets all signs, or signs that have placed by a specific person",
      authorizations = {@Authorization(value = "jwtAuth")})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Returned on successful call", response = Sign.class),
        @ApiResponse(code = 403, message = "Forbidden - Login failed")
      })
  @GetMapping(value = "/sign")
  public List<Sign> getSign(@RequestParam(value = "placer", required = false) UUID placer) {
    if (placer != null) {
      return signService.getSignsWithPlacer(placer);
    }
    return signService.getSigns();
  }

  @ApiOperation(
      value = "Gets all signs, or signs that have placed by a specific person",
      authorizations = {@Authorization(value = "jwtAuth")})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Returned on successful call", response = Sign.class),
        @ApiResponse(code = 403, message = "Forbidden - Login failed")
      })
  @GetMapping("/sign/{id}")
  public Sign getSignWithId(@PathVariable("id") UUID signId) {
    return signService.getSign(signId).orElse(null);
  }

  @ApiOperation(
      value = "Gets all signs, or signs that have placed by a specific person",
      authorizations = {@Authorization(value = "jwtAuth")})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Returned on successful call", response = Sign.class),
        @ApiResponse(code = 403, message = "Forbidden - Login failed")
      })
  //  @PreAuthorize("hasRole('SERVER')")
  @PostMapping("/sign")
  public Sign postSign(@RequestBody() SignPostDTO signPostDTO) {
    return signService.createSign(signPostDTO);
  }

  @ApiOperation(
      value = "Gets all signs, or signs that have placed by a specific person",
      authorizations = {@Authorization(value = "jwtAuth")})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Returned on successful call", response = Sign.class),
        @ApiResponse(code = 403, message = "Forbidden - Login failed")
      })
  //  @PreAuthorize("hasRole('MOD')")
  @PostMapping("/sign/{id}/request")
  public SignEvent postSignRequest(
      @RequestBody() SignEventPostDTO signEventPostDTO, @PathVariable("id") String signId) {
    return signEventService.createSignEvent(signEventPostDTO);
  }
}

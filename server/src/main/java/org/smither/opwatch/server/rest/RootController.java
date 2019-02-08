package org.smither.opwatch.server.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RootController {

  @GetMapping("/")
  public void index( HttpServletResponse response) {
    try {
      response.sendRedirect("/swagger-ui.html#/");
    } catch (IOException e) {
      return;
    }
  }

}

package org.smither.opwatch.server.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {

  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public void doFilter( ServletRequest req, ServletResponse res, FilterChain filterChain)
      throws IOException, ServletException {
    String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
    try {
      System.out.println("Token->"+token);
      if (token != null && jwtTokenProvider.validateToken(token)) {
        System.out.println("jwt Token validate->"+jwtTokenProvider.validateToken(token));
        System.out.println("Token validated");
        Authentication auth = token != null ? jwtTokenProvider.getAuthentication(token) : null;
        SecurityContextHolder.getContext().setAuthentication(auth);
        //        ((HttpServletResponse)res).setStatus(validToken?HttpServletResponse
        // .SC_OK:HttpServletResponse.SC_FORBIDDEN);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      HttpServletResponse response = (HttpServletResponse) res;
      response.sendError(403, "Forbidden");
      return;
    }
    filterChain.doFilter(req, res);
  }

}
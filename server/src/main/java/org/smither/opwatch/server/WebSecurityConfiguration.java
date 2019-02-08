package org.smither.opwatch.server;

import org.smither.opwatch.server.auth.JwtTokenFilterConfiguration;
import org.smither.opwatch.server.auth.JwtTokenProvider;
import org.smither.opwatch.server.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private UserService userService;

  private JwtTokenProvider jwtTokenProvider;

  private JwtTokenFilterConfiguration jwtTokenFilterConfigurer;

  @Autowired
  public WebSecurityConfiguration(
      UserService userService, JwtTokenProvider jwtTokenProvider,
      JwtTokenFilterConfiguration jwtTokenFilterConfigurer
  ) {
    this.userService = userService;
    this.jwtTokenProvider = jwtTokenProvider;
    this.jwtTokenFilterConfigurer = jwtTokenFilterConfigurer;
  }

  @Override
  protected void configure( HttpSecurity http ) throws Exception {
    // Disable CSRF (cross site request forgery)
    http.cors().disable(); // TODO REMOVE CORS DISABLE
    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // Entry points
    http.authorizeRequests()
        .antMatchers(HttpMethod.POST, "/user")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/token")
        .permitAll()
        // Disallow everything else..
        .anyRequest()
        .authenticated();
    http.apply(jwtTokenFilterConfigurer);
  }

  @Override
  public void configure( WebSecurity web ) throws Exception {
    // Allow swagger to be accessed without authentication
    web.ignoring().antMatchers("/v2/api-docs")
        .antMatchers("/swagger-resources/**")
        .antMatchers("/swagger-ui.html")
        .antMatchers("/configuration/**")
        .antMatchers("/webjars/**")
        .antMatchers("/ws/**")
        .antMatchers("/public")
        .antMatchers("/**"); // TODO: REMOVE THIS - DISABLES ALL AUTH
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedHeaders(
        Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Origin", "Referer",
            "User-Agent"
        ));
    configuration.setAllowCredentials(true);
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
package org.smither.opwatch.server;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket api() {
    List<SecurityScheme> schemeList = new ArrayList<>();
    schemeList.add(new BasicAuth("basicAuth"));
    schemeList.add(new ApiKey("jwtAuth", "Authorization", "header"));
    HashSet<String> contentTypes=new HashSet<>();
    contentTypes.add("application/json");
    return new Docket(DocumentationType.SWAGGER_2).ignoredParameterTypes(
        AuthenticationPrincipal.class)
        .useDefaultResponseMessages(false)
        .produces(contentTypes)
        .consumes(contentTypes)
        .securitySchemes(schemeList)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(paths())
        .build();
  }

  private ApiInfo metaData() {
    ApiInfo apiInfo = new ApiInfo("OpWatch API",
        "API for interacting with OpWatch", "0.0", "",
        new Contact("", "", ""), "", "", new ArrayList<VendorExtension>());
    return apiInfo;
  }

  private Predicate<String> paths() {
    return or(regex(".*"));
  }

}
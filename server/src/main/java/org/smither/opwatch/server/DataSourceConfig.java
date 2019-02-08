package org.smither.opwatch.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {

  private String driverClassName;

  private String url;

  private String dbUsername;

  private String dbPassword;

  private DriverManagerDataSource dataSource;

  @Autowired
  DataSourceConfig(
      @Value("${spring.datasource.driver-class-name}") String driverClassName,
      @Value("${spring.datasource.url}") String url,
      @Value("${spring.datasource.username}") String dbUsername,
      @Value("${spring.datasource.password}") String dbPassword
  ) {
    this.driverClassName = driverClassName;
    this.url = url;
    this.dbUsername = dbUsername;
    this.dbPassword = dbPassword;
  }

  public DriverManagerDataSource getDataSource() {
    if (dataSource == null) {
      dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName(driverClassName);
      dataSource.setUrl(url);
      dataSource.setUsername(dbUsername);
      dataSource.setPassword(dbPassword);
    }
    return dataSource;
  }

}

package com.epam.esm.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm.repository")
@PropertySource("classpath:repository.properties")
public class SpringJdbcConfig {

    private final String URL = "jdbc";
    private final String USER = "user";
    private final String DRIVER = "driver";
    private final String PASSWORD = "password";
    @Autowired
    Environment env;

    @Bean
    DataSource dataSource() {
        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty(DRIVER));//("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl(env.getProperty(URL));
        dataSource.setUsername(env.getProperty(USER));
        dataSource.setPassword(env.getProperty(PASSWORD));

        return dataSource;
    }

}
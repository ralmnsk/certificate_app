package com.epam.esm.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * The type Repository configuration.
 * The configuration is created with Spring component scan automatically.
 */
@Configuration
@ComponentScan("com.epam.esm.repository")
@Import(SpringJdbcConfig.class)
public class RepositoryConfiguration {
    /**
     * {@link org.springframework.jdbc.core.JdbcTemplate} jdbc template.
     *
     * @param dataSource the data source is injected into jdbc template
     * @return the jdbc template
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

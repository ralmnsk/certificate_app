package com.epam.esm.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * The type Spring jdbc config.
 */
@Configuration
@PropertySource("classpath:repository.properties")
public class SpringJdbcConfig {

    @Value("${jdbc}")
    private String url;
    @Value("${user}")
    private String user;
    @Value("${driver}")
    private String driver;
    @Value("${password}")
    private String password;
    @Value("${pool.size}")
    private String poolSize;
    @Value("${minimum.idle}")
    private String minIdle;


    /**
     * Data source data source.
     *
     * @return the data source
     */
    @Bean
    DataSource dataSource() {
        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setMaximumPoolSize(Integer.parseInt(poolSize));
        dataSource.setMinimumIdle(Integer.parseInt(minIdle));

        return dataSource;
    }

}
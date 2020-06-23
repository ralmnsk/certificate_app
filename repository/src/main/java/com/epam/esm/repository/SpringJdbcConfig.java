package com.epam.esm.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * The type Spring jdbc config.
 * The configuration includes {@link DataSourceTransactionManager}
 * and {@link com.zaxxer.hikari.HikariDataSource} for creating of an configurable
 * connection pool.
 * The properties of the data source (connection pool)
 * are loaded from the repository.properties file.
 */
@Configuration
@PropertySource("classpath:repository.properties")
@EnableTransactionManagement
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
     * The HikariCP pooled DataSource bean.
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

    /**
     * The transaction manager to control JDBC transactions.
     *
     * @return platform transaction manager
     */

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

}
package com.epam.esm.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.epam.esm.repository")
@EnableTransactionManagement
@EntityScan(basePackages = "com.epam.esm.model")
public class RepositoryConfiguration {
}

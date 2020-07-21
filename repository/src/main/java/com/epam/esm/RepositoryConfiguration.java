package com.epam.esm;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
@EntityScan(basePackages = "com.epam.esm.model")
public class RepositoryConfiguration {
}

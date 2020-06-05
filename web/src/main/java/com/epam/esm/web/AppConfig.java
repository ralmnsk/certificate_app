package com.epam.esm.web;

import com.epam.esm.repository.RepositoryConfiguration;
import com.epam.esm.service.ServiceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.epam.esm.web"})
@Import({RepositoryConfiguration.class, ServiceConfiguration.class})
public class AppConfig implements WebMvcConfigurer {
}


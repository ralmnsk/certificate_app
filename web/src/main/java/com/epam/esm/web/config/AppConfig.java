package com.epam.esm.web.config;

import com.epam.esm.service.ServiceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type App config.
 * The configuration file of the application web layer contains
 * the service configuration.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.epam.esm.web"})
@Import({ServiceConfiguration.class})
public class AppConfig implements WebMvcConfigurer {
}


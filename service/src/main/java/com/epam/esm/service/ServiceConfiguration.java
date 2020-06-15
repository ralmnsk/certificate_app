package com.epam.esm.service;

import com.epam.esm.repository.RepositoryConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"com.epam.esm.service"})
@Import({RepositoryConfiguration.class}) //Integration test for collections of main entities
public class ServiceConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

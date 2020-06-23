package com.epam.esm.service;

import com.epam.esm.repository.RepositoryConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * The type Service configuration.
 * The configuration of the application service layer includes
 * an repository layer, {@link ModelMapper} bean and
 * {@link @MethodValidationPostProcessor} bean.
 */
@Configuration
@ComponentScan({"com.epam.esm.service"})
@Import({RepositoryConfiguration.class})
public class ServiceConfiguration {
    /**
     * Model mapper to convert the object of one class to the object of other class.
     *
     * @return the model mapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Method validation post processor.
     * A convenient {@link BeanPostProcessor} implementation that delegates to a
     * JSR-303 provider for performing method-level validation on annotated methods.
     *
     * @return the method validation post processor
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}

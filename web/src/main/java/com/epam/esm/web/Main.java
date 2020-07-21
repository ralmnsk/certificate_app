package com.epam.esm.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Slf4j
@Configuration
@SpringBootApplication
@EnableSpringDataWebSupport

public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

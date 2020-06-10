package com.epam.esm.web.controller;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.Main;
import com.epam.esm.web.config.AppConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.cert.Certificate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CertificateControllerTest {
//    private CertificateController controller;
//    private CertificateDto certificateDto;
//    private TagDto tagDto;
//
//    @BeforeEach
//    void setUp() {
//        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//        controller = context.getBean(CertificateController.class);
//        certificateDto = new CertificateDto();
//        certificateDto.setCreation(Instant.now());
//        certificateDto.setDescription("description1");
//        certificateDto.setDuration(10);
//        certificateDto.setModification(Instant.now());
//        certificateDto.setName("certificate1");
//        certificateDto.setPrice(new BigDecimal(119.194).setScale(2, RoundingMode.HALF_UP));
//
//        tagDto = new TagDto();
//        tagDto.setName("tag1");
//
//        certificateDto.getTagDtos().add(tagDto);
//
//    }
//
//    @AfterEach
//    void tearDown() {
//
//    }
//
//    @Test
//    void getAll() {
//
//    }
//
//    @Test
//    void get() {
//    }
//
//    @Test
//    void create() { // -  -
//        CertificateDto testCertDto = controller.create(this.certificateDto);
//        TagDto testTagDto = new ArrayList<>(testCertDto.getTagDtos()).get(0);
//        assertEquals(testCertDto,certificateDto);
//        assertEquals(testTagDto,tagDto);
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void delete() {
//    }
}
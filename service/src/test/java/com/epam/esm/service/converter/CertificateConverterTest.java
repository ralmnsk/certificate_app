package com.epam.esm.service.converter;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.ServiceConfiguration;
import com.epam.esm.service.dto.CertificateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CertificateConverterTest {
    private Certificate one;
    private Tag tag;
    private CertificateDto oneDto;
    private Converter<CertificateDto, Certificate> certificateConverter;

    @BeforeEach
    void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ServiceConfiguration.class);
        certificateConverter = context.getBean(CertificateConverter.class);

        one = new Certificate();
        one.setName("name1");
        one.setDescription("description1");
        one.setPrice(new BigDecimal(119.194).setScale(2, RoundingMode.HALF_UP));
        one.setCreation(Instant.now());
        one.setModification(Instant.now().plus(Duration.ofDays(10)));
        one.setDuration(100);

        tag = new Tag();
        tag.setName("tagNameOne");

        one.getTags().add(tag);
    }


    @Test
    void toEntity() {
        toDto();
        Certificate certificate = certificateConverter.toEntity(oneDto);
        assertEquals(1, oneDto.getTagDtos().size());
        assertEquals(certificate.getName(), oneDto.getName());
    }

    @Test
    void toDto() {
        CertificateDto dto = certificateConverter.toDto(one);
        assertEquals(1, dto.getTagDtos().size());
        assertEquals(dto.getName(), one.getName());
        oneDto = dto;
    }

    @Test
    void testToEntityNull() {
        assertNull(certificateConverter.toEntity(null));
    }

    @Test
    void testToDtoNull() {
        assertNull(certificateConverter.toDto(null));
    }
}
package com.epam.esm.service.converter;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.ServiceConfiguration;
import com.epam.esm.service.dto.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TagConverterTest {
    private Certificate one;
    private Tag tag;
    private TagDto tagDto;
    private Converter<TagDto, Tag> tagConverter;

    @BeforeEach
    void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ServiceConfiguration.class);
        tagConverter = context.getBean(TagConverter.class);

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
        Tag tag = tagConverter.toEntity(tagDto);
        assertEquals(tag.getName(),tagDto.getName());

    }

    @Test
    void toDto() {
        TagDto tagDto = tagConverter.toDto(tag);
        assertEquals(tag.getName(),tagDto.getName());
        this.tagDto = tagDto;
    }
}
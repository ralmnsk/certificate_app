package com.epam.esm.service;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.certificate.CertificateRepositoryImpl;
import com.epam.esm.repository.certificate.tag.CertificateTagRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.repository.tag.TagRepositoryImpl;
import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.certificate.CertificateServiceImpl;
import com.epam.esm.service.converter.CertificateConverter;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.dto.CertificateDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CertificateServiceImplTest {
    private CertificateService<CertificateDto> service;
    private Tag tagOne;
    private Tag tagTwo;
    private Certificate one;
    private Certificate two;

    private CertificateRepository<Certificate, Long> certificateRepository;
    private CertificateTagRepository certificateTagRepository;
    private TagRepository<Tag, Long> tagRepository;
    private CertificateConverter certificateConverter;
    private TagConverter tagConverter;


    @BeforeEach
    void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ServiceConfiguration.class);
        service = context.getBean(CertificateServiceImpl.class);
        certificateRepository = context.getBean(CertificateRepositoryImpl.class);
        certificateTagRepository = context.getBean(CertificateTagRepository.class);
        tagRepository = context.getBean(TagRepositoryImpl.class);
        certificateConverter = context.getBean(CertificateConverter.class);
        tagConverter = context.getBean(TagConverter.class);

        one = new Certificate();
        one.setName("name1");
        one.setDescription("description1");
        one.setPrice(new BigDecimal(119.194).setScale(2, RoundingMode.HALF_UP));
        one.setCreation(Instant.now());
        one.setModification(Instant.now().plus(Duration.ofDays(10)));
        one.setDuration(100);

        two = new Certificate();
        two.setName("name2");
        two.setDescription("description2");
        two.setPrice(new BigDecimal(18.186).setScale(2, RoundingMode.HALF_UP));
        two.setCreation(Instant.now().minus(Duration.ofDays(1)));
        two.setModification(Instant.now().plus(Duration.ofDays(20)));
        two.setDuration(50);

        tagOne = new Tag();
        tagOne.setName("tag1");

        tagTwo = new Tag();
        tagTwo.setName("tag2");
    }

    @AfterEach
    void remove() {

    }


    @Test
    void CertificateNoInDbAndTagNoInDb() {
        one.getTags().add(tagOne);
        CertificateDto oneDto = certificateConverter.toDto(one);
        Optional<CertificateDto> save = service.save(oneDto);
        assertTrue(save.isPresent());

        Optional<Tag> byName = tagRepository.getByName(tagOne.getName());
        assertTrue(byName.isPresent());

        List<Long> tagIdsByCertificateId = certificateTagRepository.getTagIdsByCertificateId(save.get().getId());
        assertTrue(tagIdsByCertificateId.size() > 0);

        certificateTagRepository.deleteCertificateTag(save.get().getId(),byName.get().getId());
        service.delete(save.get().getId());
        tagRepository.delete(byName.get().getId());
    }

    @Test
    @Transactional
    void CertificateNoInDbAndTagInDb() {
        Optional<Tag> tagOptional = tagRepository.save(tagOne);
        assertTrue(tagOptional.isPresent());

        one.getTags().add(tagOne);
        CertificateDto oneDto = certificateConverter.toDto(one);
        Optional<CertificateDto> save = service.save(oneDto);
        assertTrue(save.isPresent());

        Optional<Tag> byName = tagRepository.getByName(tagOne.getName());
        assertTrue(byName.isPresent());

        List<Long> tagIdsByCertificateId = certificateTagRepository.getTagIdsByCertificateId(save.get().getId());
        assertTrue(tagIdsByCertificateId.size() > 0);

        certificateTagRepository.deleteCertificateTag(save.get().getId(),byName.get().getId());
        service.delete(save.get().getId());
        tagRepository.delete(byName.get().getId());
    }

}
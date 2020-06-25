package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Filter;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.certificate.CertificateRepositoryImpl;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.repository.tag.TagRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CertificateRepositoryImplTest {

    private CertificateRepository<Certificate, Long> repository;
    private Certificate one;
    private Certificate two;
    private TagRepository<Tag, Integer> tagRepository;

    @BeforeEach
    void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
        repository = context.getBean(CertificateRepositoryImpl.class);
        tagRepository = context.getBean(TagRepositoryImpl.class);

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
    }

    @AfterEach
    void tearDown() {
        List<Certificate> byName = repository.getByName(one.getName());
        if (!byName.isEmpty()) {
            byName.forEach(certificate -> repository.delete(certificate.getId()));
        }
    }

    @Test
    void getByName() {
        repository.save(one);
        List<Certificate> byLogin = repository.getByName(one.getName());
        assertEquals(byLogin.get(0).getName(), one.getName());
    }

    @Test
    void getAll() {
        repository.save(one);
        repository.save(two);
        Filter filter = new Filter();
        filter.setName("");
        filter.setTagName("");
        filter.setPage(1);
        filter.setSize(1000);
        List<String> sortParams = new ArrayList<>();
        sortParams.add("name");
        sortParams.add("creation");
        filter.setSortParams(sortParams);
        List<Certificate> certificates = repository.getAll(filter);
        assertTrue(certificates.size() > 0);
        assertTrue(certificates
                .stream()
                .filter(u -> u.getName().equals(one.getName()))
                .collect(Collectors.toList()).size() > 0);
        assertTrue(certificates
                .stream()
                .filter(u -> u.getName().equals(two.getName()))
                .collect(Collectors.toList()).size() > 0);
        repository.delete(repository.getByName(two.getName()).get(0).getId());
    }


    @Test
    void save() {
        assertTrue(repository.save(one).isPresent());
    }

    @Test
    void get() {
        repository.save(one);
        List<Certificate> byLogin = repository.getByName(one.getName());
        assertEquals(byLogin.get(0).getName(), one.getName());
    }

    @Test
    void update() {
        repository.save(one);
        List<Certificate> read = repository.getByName(one.getName());
        read.get(0).setName("new_name");
        read.get(0).setDescription("new_description");
        repository.update(read.get(0));
        Optional<Certificate> updated = repository.get(read.get(0).getId());
        assertEquals("new_name", updated.get().getName());
        assertEquals("new_description", updated.get().getDescription());
        one = updated.get();
    }

    @Test
    void delete() {
        repository.save(one);
        List<Certificate> certificate = repository.getByName(one.getName());
        Long id = certificate.get(0).getId();
        assertTrue(repository.delete(id));
    }


    @Test
    void getByLoginNoInDB() {
        List<Certificate> certificates = repository.getByName("no");
        assertTrue(certificates.isEmpty());
    }

    @Test
    void getByIdNoInDB() {
        Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> repository.get(111111111L));
    }

    @Test
    void saveAlreadyExists() throws Exception {
        repository.save(one);
        Optional<Certificate> saved = repository.save(one);
        assertTrue(saved.isPresent());
    }

    @Test
    void deleteNoInDB() {
        boolean deleted = repository.delete(1111133333L);
        assertFalse(deleted);
    }

    @Test
    void getAllCount() {
        Filter filter = new Filter();
        filter.setTagName("");
        filter.setName("name1");
        filter.setPage(1);
        filter.setSize(10);
        filter.setTurnCountingOn(true);
        filter.setSortParams(new ArrayList<String>(Arrays.asList("name", "creation")));
        repository.save(one);
        Long count = repository.getAllCount(filter);
        assertEquals(count, 1L);
    }

    @Test
    void testSaveCertificateTag() {
        Tag tag = new Tag();
        tag.setName("commodity");
        one.getTags().add(tag);

        Optional<Certificate> testCertOpt = repository.save(one);
        Optional<Tag> testTagOpt = tagRepository.save(tag);

        Certificate cert = testCertOpt.get();
        Tag tg = testTagOpt.get();

        repository.saveCertificateTag(cert.getId(), tg.getId());
        List<Tag> tags = tagRepository.getTagsByCertificateId(cert.getId());
        assertEquals(tags.get(0).getName(), tag.getName());

        repository.deleteCertificateTag(cert.getId(), tg.getId());
        tagRepository.delete(tg.getId());
    }
}
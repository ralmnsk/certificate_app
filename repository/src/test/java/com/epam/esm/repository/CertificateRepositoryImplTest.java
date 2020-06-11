package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.certificate.CertificateRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CertificateRepositoryImplTest {

    private CertificateRepository<Certificate,Long> repository;
    private Certificate one;
    private Certificate two;

    @BeforeEach
    void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringJdbcConfig.class);
        repository = context.getBean(CertificateRepositoryImpl.class);

        one = new Certificate();
        one.setName("name1");
        one.setDescription("description1");
        one.setPrice(new BigDecimal(119.194).setScale(2,RoundingMode.HALF_UP));
        one.setCreation(Instant.now());
        one.setModification(Instant.now().plus(Duration.ofDays(10)));
        one.setDuration(100);

        two = new Certificate();
        two.setName("name2");
        two.setDescription("description2");
        two.setPrice(new BigDecimal(18.186).setScale(2,RoundingMode.HALF_UP));
        two.setCreation(Instant.now().minus(Duration.ofDays(1)));
        two.setModification(Instant.now().plus(Duration.ofDays(20)));
        two.setDuration(50);
    }

    @AfterEach
    void tearDown() {
        Optional<Certificate> byName = repository.getByName(one.getName());
        byName.ifPresent(certificate -> repository.delete(certificate.getId()));
    }

    @Test
    void getByName() {
        repository.save(one);
        Optional<Certificate> byLogin = repository.getByName(one.getName());
        assertTrue(byLogin.get().getName().equals(one.getName()));
    }

    @Test
    void getAll() {
//        repository.save(one);
//        repository.save(two);
//        List<Certificate> certificates = repository.getAll();
//        assertTrue(certificates.size() > 0);
//        assertTrue(certificates
//                .stream()
//                .filter(u -> u.getName().equals(one.getName()))
//                .collect(Collectors.toList()).size() > 0);
//        assertTrue(certificates
//                .stream()
//                .filter(u -> u.getName().equals(two.getName()))
//                .collect(Collectors.toList()).size() > 0);
//        repository.delete(repository.getByName(two.getName()).get().getId());
    }

    @Test
    void getAllNoInDB() throws Exception{
//        List<Certificate> certificates = repository.getAll();
//        assertTrue(certificates
//                .stream()
//                .filter(u -> u.getName().equals(one.getName()))
//                .collect(Collectors.toList()).size() == 0);
    }

    @Test
    void save() {
        assertTrue(repository.save(one).isPresent());
    }

    @Test
    void get() {
        repository.save(one);
        Optional<Certificate> byLogin = repository.getByName(one.getName());
        assertEquals(byLogin.get().getName(), one.getName());
    }

    @Test
    void update() throws Exception{
        repository.save(one);
        Optional<Certificate> read = repository.getByName(one.getName());
        read.get().setName("new_name");
        read.get().setDescription("new_description");
        repository.update(read.get());
        Optional<Certificate> updated = repository.get(read.get().getId());
        assertEquals("new_name", updated.get().getName());
        assertEquals("new_description", updated.get().getDescription());
        one = updated.get();
        assertFalse(repository.update(two).isPresent());
    }

    @Test
    void delete() {
        repository.save(one);
        Optional<Certificate> certificate = repository.getByName(one.getName());
        Long id = certificate.get().getId();
        repository.delete(id);
        Optional<Certificate> check = repository.get(id);
        assertFalse(check.isPresent());
    }


    @Test
    void getByLoginNoInDB(){
        Optional<Certificate> certificate = repository.getByName("no");
        assertFalse(certificate.isPresent());
    }

    @Test
    void getByIdNoInDB(){
        Optional<Certificate> certificate = repository.get(111111111L);
        assertFalse(certificate.isPresent());
    }

    @Test
    void saveAlreadyExists() throws Exception{
        repository.save(one);
        Optional<Certificate> saved = repository.save(one);
        assertFalse(saved.isPresent());
    }

    @Test
    void deleteNoInDB(){
        boolean deleted = repository.delete(1111133333L);
        assertFalse(deleted);
    }
}
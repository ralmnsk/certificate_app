package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CertificateRepositoryDefaultTest {

    private CertificateRepository<Certificate> repository;
    private Certificate one;
    private Certificate two;

    @BeforeEach
    void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringJdbcConfig.class);
        repository = context.getBean(CertificateRepositoryDefault.class);

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
        Certificate byName = repository.getByName(one.getName());
        if (byName != null){
            repository.delete(byName.getId());
        }
    }

    @Test
    void getByName() {
        repository.save(one);
        Certificate byLogin = repository.getByName(one.getName());
        assertTrue(byLogin.getName().equals(one.getName()));
    }

    @Test
    void getAll() {
        repository.save(one);
        repository.save(two);
        List<Certificate> certificates = repository.getAll();
        assertTrue(certificates.size() > 0);
        assertTrue(certificates
                .stream()
                .filter(u -> u.getName().equals(one.getName()))
                .collect(Collectors.toList()).size() > 0);
        assertTrue(certificates
                .stream()
                .filter(u -> u.getName().equals(two.getName()))
                .collect(Collectors.toList()).size() > 0);
        repository.delete(repository.getByName(two.getName()).getId());
    }

    @Test
    void getAllNoInDB() throws Exception{
        List<Certificate> certificates = repository.getAll();
        assertTrue(certificates
                .stream()
                .filter(u -> u.getName().equals(one.getName()))
                .collect(Collectors.toList()).size() == 0);
    }

    @Test
    void save() {
        assertTrue(repository.save(one));
    }

    @Test
    void get() {
        repository.save(one);
        Certificate byLogin = repository.getByName(one.getName());
        assertEquals(byLogin.getName(), one.getName());
    }

    @Test
    void update() throws Exception{
        repository.save(one);
        Certificate read = repository.getByName(one.getName());
        read.setName("new_name");
        read.setDescription("new_description");
        repository.update(read);
        Certificate updated = repository.get(read.getId());
        assertEquals("new_name", updated.getName());
        assertEquals("new_description", updated.getDescription());
        one = updated;
        assertFalse(repository.update(two));
    }

    @Test
    void delete() {
        repository.save(one);
        Certificate certificate = repository.getByName(one.getName());
        Long id = certificate.getId();
        repository.delete(id);
        Certificate check = repository.get(id);
        assertNull(check);
    }


    @Test
    void getByLoginNoInDB(){
        Certificate certificate = repository.getByName("no");
        assertNull(certificate);
    }

    @Test
    void getByIdNoInDB(){
        Certificate certificate = repository.get(111111111L);
        assertNull(certificate);
    }

    @Test
    void saveAlreadyExists() throws Exception{
        repository.save(one);
        boolean saved = repository.save(one);
        assertFalse(saved);
    }

    @Test
    void deleteNoInDB(){
        boolean deleted = repository.delete(1111133333L);
        assertFalse(deleted);
    }
}
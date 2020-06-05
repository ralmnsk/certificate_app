package com.epam.esm.service;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.service.certificate.CertificateServiceDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;

class CertificateServiceDefaultTest {
    @Mock
    private CertificateRepository<Certificate> repository;
    @InjectMocks
    private CertificateServiceDefault service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void getByName() {
        service.getByName(any());
        Mockito.verify(repository).getByName(any());
    }

    @Test
    void getAll() {
        service.getAll();
        Mockito.verify(repository).getAll();
    }

    @Test
    void save() {
        service.save(any());
        Mockito.verify(repository).save(any());
    }

    @Test
    void get() {
        service.get(any());
        Mockito.verify(repository).get(any());
    }

    @Test
    void update() {
        service.update(any());
        Mockito.verify(repository).update(any());
    }

    @Test
    void delete() {
        service.delete(any());
        Mockito.verify(repository).delete(any());
    }
}
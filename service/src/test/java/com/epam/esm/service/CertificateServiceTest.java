package com.epam.esm.service;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Filter;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.service.certificate.CertificateServiceImpl;
import com.epam.esm.service.converter.CertificateConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;

class CertificateServiceTest {
    @Mock
    private CertificateRepository<Certificate,Long> repository;
    @Mock
    private CertificateConverter certificateConverter;

    @InjectMocks
    private CertificateServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void getAll() {
        Filter filter = new Filter();
        service.getAll(filter);
        Mockito.verify(repository).getAll(filter);
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
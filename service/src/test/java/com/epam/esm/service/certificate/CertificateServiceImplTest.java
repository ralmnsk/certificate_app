package com.epam.esm.service.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.filter.CertificateFilter;
import com.epam.esm.model.wrapper.CertificateListWrapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.filter.CertificateFilterDto;
import com.epam.esm.service.impl.CertificateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CertificateServiceImplTest {
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private CertificateServiceImpl certificateService;

    private Order order;
    private Certificate certificate;
    private CertificateDto certificateDto;
    private Tag tag;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        order = new Order();
        order.setDeleted(false);
        order.setTotalCost(new BigDecimal(0));
        order.setDescription("description");
        order.setCreated(Timestamp.from(Instant.now()));
        order.setId(1L);

        certificate = new Certificate();
        certificate.setDeleted(false);
        certificate.setDuration(10);
        certificate.setDescription("certificate description");
        certificate.setPrice(new BigDecimal(50));
        certificate.setName("name");
        certificate.setId(2L);
        certificate.setCreation(order.getCreated());
        order.getCertificates().add(certificate);
        modelMapper = new ModelMapper();
        tag = new Tag();
        tag.setName("testTag");
        tag.setId(1);
        certificate.getTags().add(tag);

        certificateDto = modelMapper.map(certificate, CertificateDto.class);
        when(certificateRepository.save(any())).thenReturn(Optional.ofNullable(certificate));
        when(certificateRepository.get(any())).thenReturn(Optional.ofNullable(certificate));
        when(certificateRepository.update(any())).thenReturn(Optional.ofNullable(certificate));
    }

    @Test
    void save() {
        certificateService.save(certificateDto);
        verify(certificateRepository).save(any());
    }

    @Test
    void get() {
        certificateService.get(any());
        verify(certificateRepository).get(any());
    }

    @Test
    void update() {
        certificateService.update(certificateDto);
        verify(certificateRepository).update(any());
    }

    @Test
    void delete() {
        certificateService.delete(any());
        verify(certificateRepository).delete(any());
    }

    @Test
    void getAll() {
        CertificateFilterDto filterDto = new CertificateFilterDto();
        CertificateFilter filter = new CertificateFilter();
        when(mapper.map(filterDto, CertificateFilter.class)).thenReturn(filter);
        when(mapper.map(certificate, CertificateDto.class)).thenReturn(certificateDto);
        CertificateListWrapper wrapper = new CertificateListWrapper();
        List<Certificate> certificates = new ArrayList<>();
        certificates.add(certificate);
        wrapper.setFilter(filter);
        wrapper.setList(certificates);

        when(certificateRepository.getAll(filter)).thenReturn(wrapper);
        certificateService.getAll(filterDto);
        verify(certificateRepository).getAll(filter);
    }

    @Test
    void addCertificateToOrder() {
        Set<Long> set = new HashSet<>();
        set.add(1L);
        when(orderRepository.get(1L)).thenReturn(Optional.ofNullable(order));
        when(orderRepository.update(order)).thenReturn(Optional.ofNullable(order));
        certificateService.addCertificateToOrder(1L, set);
        verify(orderRepository).update(order);
    }

    @Test
    void deleteCertificateFromOrder() {
        Set<Long> set = new HashSet<>();
        set.add(1L);
        when(orderRepository.get(1L)).thenReturn(Optional.ofNullable(order));
        when(orderRepository.update(order)).thenReturn(Optional.ofNullable(order));
        certificateService.addCertificateToOrder(1L, set);
        verify(orderRepository).update(order);
    }
}
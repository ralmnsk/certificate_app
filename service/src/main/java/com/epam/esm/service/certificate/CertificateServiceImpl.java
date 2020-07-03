package com.epam.esm.service.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.OrderRepository;
import com.epam.esm.service.converter.CertificateConverter;
import com.epam.esm.service.dto.CertificateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CertificateServiceImpl implements CertificateService<CertificateDto, Long> {

    private CertificateRepository certificateRepository;
    private OrderRepository orderRepository;
    private CertificateConverter certificateConverter;

    public CertificateServiceImpl(CertificateRepository certificateRepository, OrderRepository orderRepository, CertificateConverter certificateConverter) {
        this.certificateRepository = certificateRepository;
        this.orderRepository = orderRepository;
        this.certificateConverter = certificateConverter;
    }

    @Override
    public Optional<CertificateDto> save(CertificateDto certificateDto) {
        Optional<CertificateDto> certificateDtoOptional = Optional.empty();
        if (certificateDto != null) {
            Certificate certificate = certificateConverter.toEntity(certificateDto);
            certificateRepository.saveAndFlush(certificate);
            return get(certificate.getId());
        }
        return certificateDtoOptional;
    }

    @Override
    public Optional<CertificateDto> get(Long id) {
        Optional<CertificateDto> certificateDtoOptional = Optional.empty();
        Certificate certificate = certificateRepository.getOne(id);
        setCorrectTime(certificate);
        certificateDtoOptional = Optional.ofNullable(certificateConverter.toDto(certificate));

        return certificateDtoOptional;
    }

    @Override
    public Optional<CertificateDto> update(CertificateDto certificateDto) {
        return save(certificateDto);
    }

    @Override
    public boolean delete(Long certId) {
        certificateRepository.deleteById(certId);
        return true;
    }

    @Override
    public Page<CertificateDto> getAll(Pageable pageable) {
        Page<Certificate> users = certificateRepository.findAll(pageable);
        List<CertificateDto> dtoList = users.getContent()
                .stream()
                .map(c -> certificateConverter.toDto(c))
                .collect(Collectors.toList());
        return new PageImpl<CertificateDto>(dtoList, pageable, dtoList.size());
    }

    @Override
    public Page<CertificateDto> getAllByOrderId(Long orderId, Pageable pageable) {
        Page<Certificate> certificates = certificateRepository.getAllByOrderId(orderId, pageable);
        if (certificates.getContent() == null && certificates.getContent().isEmpty()) {
            return new PageImpl<>(null, pageable, 0);
        }

        List<CertificateDto> dtoList = certificates.getContent()
                .stream()
                .map(o -> certificateConverter.toDto(o))
                .collect(Collectors.toList());
        return new PageImpl<CertificateDto>(dtoList, pageable, dtoList.size());
    }

    @Override
    public Optional<CertificateDto> createCertificateInOrder(Long orderId, CertificateDto certificateDto) {
        Optional<CertificateDto> certificateDtoOptional = Optional.empty();
        if (certificateDto.getId() != null && certificateDto.getId() > 0L) {
            certificateDtoOptional = get(certificateDto.getId());
        } else {
            certificateDtoOptional = save(certificateDto);
        }

        Certificate certificate = null;
        if (certificateDtoOptional.isPresent()) {
            certificateDto = certificateDtoOptional.get();
            certificate = certificateConverter.toEntity(certificateDto);
        }

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent() && certificate != null) {
            Order order = orderOptional.get();
            order.getCertificates().add(certificate);
            order = orderRepository.save(order);
            certificateDto = certificateConverter.toDto(certificate);
            return Optional.ofNullable(certificateDto);
        }

        return certificateDtoOptional;
    }

    @Override
    public Long getCertIdByTagId(int id) {
        Long num = 0L;
        Optional<Certificate> certOptional = certificateRepository.getCertIdByTagId(id);
        if (certOptional.isPresent()) {
            num = certOptional.get().getId();
        }

        return num;
    }

    private void setCorrectTime(Certificate certificate) {
        Instant created = certificate.getCreation();
        if (created != null) {
            certificate.setCreation(Timestamp.from(created).toLocalDateTime().toInstant(ZoneOffset.UTC));
        } else {
            certificate.setCreation(Instant.now());
        }
        Instant modified = certificate.getModification();
        if (modified != null) {
            certificate.setModification(Timestamp.from(modified).toLocalDateTime().toInstant(ZoneOffset.UTC));
        } else {
            certificate.setCreation(Instant.now());
        }
    }

}

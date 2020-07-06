package com.epam.esm.service.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.OrderRepository;
import com.epam.esm.service.converter.CertificateConverter;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.InconsistencyIdException;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.tag.TagService;
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
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CertificateServiceImpl implements CertificateService<CertificateDto, Long> {

    private CertificateRepository certificateRepository;
    private OrderRepository orderRepository;
    private CertificateConverter certificateConverter;
    private TagService<TagDto, Integer> tagService;

    public CertificateServiceImpl(CertificateRepository certificateRepository, OrderRepository orderRepository, CertificateConverter certificateConverter, TagService<TagDto, Integer> tagService) {
        this.certificateRepository = certificateRepository;
        this.orderRepository = orderRepository;
        this.certificateConverter = certificateConverter;
        this.tagService = tagService;
    }

    @Override
    public Optional<CertificateDto> save(CertificateDto certificateDto) {
        checkTagsForConsistency(certificateDto);
        Certificate certificate = certificateConverter.toEntity(certificateDto);
        certificate = certificateRepository.save(certificate);
        certificateRepository.flush();
        Optional<CertificateDto> certificateDtoOptional = get(certificate.getId());

        return certificateDtoOptional;
    }

    private void checkTagsForConsistency(CertificateDto certificateDto) {
        Set<TagDto> tags = certificateDto.getTags();
        tags
                .stream() //id:yes name:yes/not
                .filter(t -> t.getId() != null && t.getId() != 0L)
                .forEach(t -> {
                    TagDto tagDto = tagService.get(t.getId()).orElseThrow(() -> new NotFoundException(t.getId()));
                    if (t.getName() != null && !t.getName().equals(tagDto.getName())) {
                        throw new InconsistencyIdException("InconsistencyException id:" + t.getId() + " and name:" + tagDto.getName());
                    } else if (t.getName() == null) {
                        t.setName(tagDto.getName());
                    }
                });
        tags
                .stream()
                .filter(t -> t.getId() == null)
                .forEach(t -> {
                    Optional<TagDto> byName = tagService.getByName(t.getName());
                    if (byName.isPresent()) {
                        Integer id = byName.get().getId();
                        t.setId(id);
                    } else {
                        TagDto tagDto = tagService.save(t).orElseThrow(() -> new SaveException("tag save exception"));
                        t.setId(tagDto.getId());
                    }
                });
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
        certificateRepository.findById(certificateDto.getId()).orElseThrow(() -> new NotFoundException(certificateDto.getId()));
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
        if (certificates.getContent().isEmpty()) {
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
        Instant created = certificateRepository.getCreationById(certificate.getId());
        if (created != null) {
            certificate.setCreation(Timestamp.from(created).toLocalDateTime().toInstant(ZoneOffset.UTC));
        }
        Instant modified = certificateRepository.getModificationById(certificate.getId());
        if (modified != null) {
            certificate.setModification(Timestamp.from(modified).toLocalDateTime().toInstant(ZoneOffset.UTC));
        }
    }

}

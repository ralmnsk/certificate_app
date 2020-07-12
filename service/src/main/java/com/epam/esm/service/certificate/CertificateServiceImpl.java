package com.epam.esm.service.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Filter;
import com.epam.esm.model.Order;
import com.epam.esm.repository.crud.CertificateCrudRepository;
import com.epam.esm.repository.crud.OrderCrudRepository;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.IdDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.exception.UpdateException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@Getter
public class CertificateServiceImpl implements CertificateService<CertificateDto, Long> {
    private FilterDto filterDto;

    private CertificateCrudRepository certificateRepository;
    private ModelMapper mapper;
    private OrderCrudRepository orderRepository;

    public CertificateServiceImpl(CertificateCrudRepository certificateRepository, ModelMapper mapper, OrderCrudRepository orderCrudRepository) {
        this.certificateRepository = certificateRepository;
        this.mapper = mapper;
        this.orderRepository = orderCrudRepository;
    }

    @Override //save without tags
    public Optional<CertificateDto> save(CertificateDto certificateDto) {
        certificateDto.getTags().clear();
        certificateDto.setId(null); //or detached entity passes to persistent and happens exception
        Certificate certificate = mapper.map(certificateDto, Certificate.class);
        certificate = certificateRepository.save(certificate).orElseThrow(() -> new SaveException("Certificate save exception"));
        return get(certificate.getId());
    }

    @Override
    public Optional<CertificateDto> get(Long id) {
        Optional<CertificateDto> certificateDtoOptional = Optional.empty();
        Certificate certificate = certificateRepository.get(id).orElseThrow(() -> new NotFoundException("Certificate not found exception, id:" + id));
        certificateDtoOptional = Optional.ofNullable(mapper.map(certificate, CertificateDto.class));

        return certificateDtoOptional;
    }

    @Override
    public Optional<CertificateDto> update(CertificateDto certificateDto) {
        long id = certificateDto.getId();
        Certificate found = certificateRepository.get(certificateDto.getId()).orElseThrow(() -> new NotFoundException("Certificate not found exception, id:" + id));

        found.setName(certificateDto.getName());
        found.setCreation(certificateDto.getCreation());
        found.setModification(certificateDto.getModification());
        found.setPrice(certificateDto.getPrice());
        found.setDescription(certificateDto.getDescription());
        found.setDuration(certificateDto.getDuration());

        Certificate certificate = certificateRepository.update(found).orElseThrow(() -> new SaveException("Certificate save exception"));
        CertificateDto dto = mapper.map(certificate, CertificateDto.class);

        return Optional.ofNullable(dto);
    }

    @Override
    public boolean delete(Long certId) {
        Certificate certificate = certificateRepository.get(certId).orElseThrow(() -> new NotFoundException("Certificate delete: not found exception, id:" + certId));
        certificate.setDeleted(true);
        certificateRepository.update(certificate).orElseThrow(() -> new UpdateException("Certificate update in delete operation exception"));
        return true;
    }

    @Override
    public List<CertificateDto> getAll(FilterDto filterDto) {
        Filter filter = mapper.map(filterDto, Filter.class);
        List<Certificate> certificates = certificateRepository.getAll(filter);
        List<CertificateDto> dtoList = new ArrayList<>();
        for (Certificate c : certificates) {
            CertificateDto d = mapper.map(c, CertificateDto.class);
            d.getTags().clear();
            dtoList.add(d);
        }

        filter = certificateRepository.getFilter();
        this.filterDto = mapper.map(filter, FilterDto.class);

        return dtoList;
    }

    @Override
    public void addCertificateToOrder(Long orderId, List<IdDto> list) {
        Order order = orderRepository.get(orderId).orElseThrow(() -> new NotFoundException("Add Certificate to Order: order not found: id:" + orderId));
        list
                .stream()
                .map(idDto -> certificateRepository.get(idDto.getId()).orElseThrow(() -> new NotFoundException("Add Certificate to Order: Certificate not found: id:" + idDto.getId())))
                .forEach(certificate -> order.getCertificates().add(certificate));
        orderRepository.update(order).orElseThrow(() -> new UpdateException("Add Certificate to Order: Certificate update exception"));
    }

    @Override
    public void deleteCertificateFromOrder(Long orderId, List<IdDto> list) {
        Order order = orderRepository.get(orderId).orElseThrow(() -> new NotFoundException("Delete Certificate from Order: Certificate not found: id:" + orderId));
        list
                .stream()
                .map(idDto -> orderRepository.get(idDto.getId()).orElseThrow(() -> new NotFoundException("Delete Certificate to Order: Certificate not found: id:" + idDto.getId())))
                .forEach(certificate -> order.getCertificates().remove(certificate));
        orderRepository.update(order).orElseThrow(() -> new UpdateException("Delete Certificate to Order: Certificate update exception"));
    }
}

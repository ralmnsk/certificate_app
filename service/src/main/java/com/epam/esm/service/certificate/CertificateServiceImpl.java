package com.epam.esm.service.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.ListWrapper;
import com.epam.esm.model.Order;
import com.epam.esm.model.filter.CertificateFilter;
import com.epam.esm.model.filter.OrderFilter;
import com.epam.esm.repository.crud.CertificateRepository;
import com.epam.esm.repository.crud.OrderRepository;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.ListWrapperDto;
import com.epam.esm.service.dto.filter.CertificateFilterDto;
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
import java.util.Set;

@Slf4j
@Service
@Transactional
@Getter
public class CertificateServiceImpl implements CertificateService<CertificateDto, Long, CertificateFilterDto> {

    private CertificateRepository<Certificate, Long, CertificateFilter> certificateRepository;
    private ModelMapper mapper;
    private OrderRepository<Order, Long, OrderFilter> orderRepository;

    public CertificateServiceImpl(CertificateRepository<Certificate, Long, CertificateFilter> certificateRepository,
                                  ModelMapper mapper,
                                  OrderRepository<Order, Long, OrderFilter> orderRepository) {
        this.certificateRepository = certificateRepository;
        this.mapper = mapper;
        this.orderRepository = orderRepository;
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
    public ListWrapperDto<CertificateDto, CertificateFilterDto> getAll(CertificateFilterDto filterDto) {
        CertificateFilter filter = mapper.map(filterDto, CertificateFilter.class);
        ListWrapper<Certificate, CertificateFilter> wrapper = certificateRepository.getAll(filter);
        List<Certificate> certificates = wrapper.getList();
        List<CertificateDto> dtoList = new ArrayList<>();
        for (Certificate c : certificates) {
            CertificateDto d = mapper.map(c, CertificateDto.class);
            d.getTags().clear();
            dtoList.add(d);
        }

        ListWrapperDto<CertificateDto, CertificateFilterDto> wrapperDto = new ListWrapperDto<>();
        wrapperDto.setList(dtoList);
        filter = wrapper.getFilter();
        wrapperDto.setFilterDto(mapper.map(filter, CertificateFilterDto.class));

        return wrapperDto;
    }

    @Override
    public void addCertificateToOrder(Long orderId, Set<Long> set) {
        Order order = orderRepository.get(orderId).orElseThrow(() -> new NotFoundException("Add Certificate to Order: order not found: id:" + orderId));
        set
                .stream()
                .map(idDto -> certificateRepository.get(idDto).orElseThrow(() -> new NotFoundException("Add Certificate to Order: Certificate not found: id:" + idDto)))
                .forEach(certificate -> order.getCertificates().add(certificate));
        orderRepository.update(order).orElseThrow(() -> new UpdateException("Add Certificate to Order: Certificate update exception"));
    }

    @Override
    public void deleteCertificateFromOrder(Long orderId, Set<Long> set) {
        Order order = orderRepository.get(orderId).orElseThrow(() -> new NotFoundException("Delete Certificate from Order: Certificate not found: id:" + orderId));
        set
                .stream()
                .map(idDto -> certificateRepository.get(idDto).orElseThrow(() -> new NotFoundException("Delete Certificate to Order: Certificate not found: id:" + idDto)))
                .forEach(certificate -> order.getCertificates().remove(certificate));
        orderRepository.update(order).orElseThrow(() -> new UpdateException("Delete Certificate to Order: Certificate update exception"));
    }
}

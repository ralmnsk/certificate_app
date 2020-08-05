package com.epam.esm.service.impl;

import com.epam.esm.calculator.TotalCostCalculator;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.filter.CertificateFilterDto;
import com.epam.esm.dto.wrapper.CertificateListWrapperDto;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.filter.CertificateFilter;
import com.epam.esm.model.wrapper.CertificateListWrapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.exception.DeleteException;
import com.epam.esm.repository.exception.NotFoundException;
import com.epam.esm.repository.exception.SaveException;
import com.epam.esm.repository.exception.UpdateException;
import com.epam.esm.service.CertificateService;
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
public class CertificateServiceImpl implements CertificateService {

    private CertificateRepository certificateRepository;
    private ModelMapper mapper;
    private OrderRepository orderRepository;
    private TotalCostCalculator calculator;


    public CertificateServiceImpl(CertificateRepository certificateRepository, ModelMapper mapper, OrderRepository orderRepository, TotalCostCalculator calculator) {
        this.certificateRepository = certificateRepository;
        this.mapper = mapper;
        this.orderRepository = orderRepository;
        this.calculator = calculator;
    }

    @Transactional
    @Override
    public Optional<CertificateDto> save(CertificateDto certificateDto) {
        certificateDto.getTags().clear();
        certificateDto.setId(null); //or detached entity passes to persistent and happens exception
        Certificate certificate = mapper.map(certificateDto, Certificate.class);
        certificate = certificateRepository.save(certificate).orElseThrow(() -> new SaveException("Certificate save exception"));
        return get(certificate.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CertificateDto> get(Long id) {
        Optional<CertificateDto> certificateDtoOptional = Optional.empty();
        Certificate certificate = certificateRepository.get(id).orElseThrow(() -> new NotFoundException("Certificate not found exception, id:" + id));
        certificateDtoOptional = Optional.ofNullable(mapper.map(certificate, CertificateDto.class));

        return certificateDtoOptional;
    }

    @Transactional
    @Override
    public Optional<CertificateDto> update(CertificateDto certificateDto) {
        long id = certificateDto.getId();
        if (isCertificateInAnyOrder(id)) {
            throw new DeleteException("Certificate update: certificate was included in some orders. It can't be updated. Certificate id:" + id);
        }
        Certificate found = certificateRepository.get(certificateDto.getId()).orElseThrow(() -> new NotFoundException("Certificate not found exception, id:" + id));
        //entity not managed without get
        found.setName(certificateDto.getName());
        found.setDescription(certificateDto.getDescription());
        found.setDuration(certificateDto.getDuration());

        found.setPrice(certificateDto.getPrice());
        Certificate certificate = certificateRepository.update(found).orElseThrow(() -> new SaveException("Certificate save exception"));
        recalculateTotalPrices(found);


        CertificateDto dto = mapper.map(certificate, CertificateDto.class);

        return Optional.ofNullable(dto);
    }

    private void recalculateTotalPrices(Certificate foundCertificate) {
        List<Order> orders = orderRepository.getOrdersByCertificateId(foundCertificate.getId());
        orders.stream().map(order -> calculator.calc(order)).forEach(orderRepository::update);
    }


    @Transactional
    @Override
    public boolean delete(Long certId) {
        Certificate certificate = certificateRepository.get(certId).orElseThrow(() -> new NotFoundException("Certificate delete: not found exception, id:" + certId));
        if (isCertificateInAnyOrder(certId)) {
            throw new DeleteException("Certificate delete: certificate was included in some orders. It can't be deleted. Certificate id:" + certId);
        }
        recalculateTotalPrices(certificate);
        certificateRepository.delete(certId);
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public CertificateListWrapperDto getAll(CertificateFilterDto filterDto) {
        CertificateFilter filter = mapper.map(filterDto, CertificateFilter.class);
        CertificateListWrapper wrapper = certificateRepository.getAll(filter);
        List<Certificate> certificates = wrapper.getList();
        List<CertificateDto> dtoList = new ArrayList<>();
        certificates.stream().map(c -> mapper.map(c, CertificateDto.class)).forEachOrdered(d -> {
            d.getTags().clear();
            dtoList.add(d);
        });

        CertificateListWrapperDto wrapperDto = new CertificateListWrapperDto();
        wrapperDto.setList(dtoList);
        filter = wrapper.getFilter();
        wrapperDto.setFilterDto(mapper.map(filter, CertificateFilterDto.class));

        return wrapperDto;
    }

    @Transactional
    @Override
    public void addCertificateToOrder(Long orderId, Set<Long> set) {
        Order order = orderRepository.get(orderId).orElseThrow(() -> new NotFoundException("Add Certificate to Order: order not found: id:" + orderId));
        if(order.isCompleted()){
            throw new UpdateException("CertificateService: certificates can't be added to the order. Order is completed. Order id:"+orderId);
        }
        set
                .stream()
                .map(idDto -> certificateRepository.get(idDto).orElseThrow(() -> new NotFoundException("Add Certificate to Order: Certificate not found: id:" + idDto)))
                .forEach(certificate -> order.getCertificates().add(certificate));
        orderRepository.update(order).orElseThrow(() -> new UpdateException("Add Certificate to Order: Certificate update exception"));
    }

    @Transactional
    @Override
    public void removeCertificateFromOrder(Long orderId, Set<Long> set) {
        Order order = orderRepository.get(orderId).orElseThrow(() -> new NotFoundException("Delete Certificate from Order: Certificate not found: id:" + orderId));
        if(order.isCompleted()){
            throw new UpdateException("CertificateService: certificates can't be removed from the order. Order is completed. Order id:"+orderId);
        }
        set
                .stream()
                .map(idDto -> certificateRepository.get(idDto).orElseThrow(() -> new NotFoundException("Delete Certificate to Order: Certificate not found: id:" + idDto)))
                .forEach(certificate -> order.getCertificates().remove(certificate));
        orderRepository.update(order).orElseThrow(() -> new UpdateException("Delete Certificate to Order: Certificate update exception"));
    }

    public boolean isCertificateInAnyOrder(Long certificateId) {
        Long count = certificateRepository.getCountOrdersByCertificateId(certificateId);
        return count > 0;
    }

}

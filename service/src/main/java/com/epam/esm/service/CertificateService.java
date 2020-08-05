package com.epam.esm.service;


import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.filter.CertificateFilterDto;
import com.epam.esm.dto.wrapper.CertificateListWrapperDto;

import java.util.Set;

public interface CertificateService extends CrudService<CertificateDto, Long> {

    void addCertificateToOrder(Long orderId, Set<Long> list);

    void removeCertificateFromOrder(Long orderId, Set<Long> list);

    CertificateListWrapperDto getAll(CertificateFilterDto filterDto);

    boolean isCertificateInAnyOrder(Long certificateId);

}

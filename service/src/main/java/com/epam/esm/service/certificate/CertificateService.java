package com.epam.esm.service.certificate;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.filter.AbstractFilterDto;
import com.epam.esm.service.dto.filter.CertificateFilterDto;
import com.epam.esm.service.dto.wrapper.CertificateListWrapperDto;

import java.util.Set;

public interface CertificateService extends CrudService<CertificateDto, Long> {

    void addCertificateToOrder(Long orderId, Set<Long> list);

    void deleteCertificateFromOrder(Long orderId, Set<Long> list);

    CertificateListWrapperDto getAll(CertificateFilterDto filterDto);

}

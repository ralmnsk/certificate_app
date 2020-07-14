package com.epam.esm.service.certificate;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.ListWrapperDto;
import com.epam.esm.service.dto.filter.AbstractFilterDto;

import java.util.Set;

public interface CertificateService<T, E, F extends AbstractFilterDto> extends CrudService<T, E> {

    void addCertificateToOrder(Long orderId, Set<Long> list);

    void deleteCertificateFromOrder(Long orderId, Set<Long> list);

    ListWrapperDto<T, F> getAll(F filterDto);

}

package com.epam.esm.service.certificate;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.IdDto;

import java.util.List;

public interface CertificateService<T, E> extends CrudService<T, E> {

    void addCertificateToOrder(Long orderId, List<IdDto> list);

    void deleteCertificateFromOrder(Long orderId, List<IdDto> list);
}

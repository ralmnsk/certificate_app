package com.epam.esm.service.certificate;


import com.epam.esm.service.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CertificateService<T, E> extends CrudService<T, E> {

    Page<T> getAllByOrderId(E orderId, Pageable pageable);

    Optional<T> createCertificateInOrder(E orderId, T certificateDto);

    E getCertIdByTagId(int id);



}

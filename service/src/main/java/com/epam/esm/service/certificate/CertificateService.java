package com.epam.esm.service.certificate;


import com.epam.esm.service.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CertificateService<T, E> extends CrudService<T, E> {

//    Page<T> getAllByOrderId(E orderId, Pageable pageable);

}

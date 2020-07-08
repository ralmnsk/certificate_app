package com.epam.esm.service.order;


import com.epam.esm.service.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService<T, E> extends CrudService<T, E> {
    Page<T> getAllByUserId(E userId, Pageable pageable);

    Optional<T> createOrderInUser(E userId, T orderDto);

//    E getOrderByCertificateId(E e);

}

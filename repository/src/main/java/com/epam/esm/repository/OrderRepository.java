package com.epam.esm.repository;

import com.epam.esm.model.Order;
import com.epam.esm.model.filter.OrderFilter;
import com.epam.esm.model.wrapper.OrderListWrapper;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    OrderListWrapper getAll(OrderFilter filter);

    List<Order> getOrdersByCertificateId(Long certificateId);

    Order getFirstByUserId(Long userId);
}

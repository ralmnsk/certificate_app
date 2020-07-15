package com.epam.esm.repository.crud;

import com.epam.esm.model.Order;
import com.epam.esm.model.filter.OrderFilter;
import com.epam.esm.model.wrapper.OrderListWrapper;
import com.epam.esm.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
    OrderListWrapper getAll(OrderFilter filter);
}

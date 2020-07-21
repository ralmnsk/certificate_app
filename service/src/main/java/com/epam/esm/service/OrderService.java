package com.epam.esm.service;


import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.filter.OrderFilterDto;
import com.epam.esm.dto.wrapper.OrderListWrapperDto;

import java.util.Set;

public interface OrderService extends CrudService<OrderDto, Long> {
    void addOrderToUser(Long userId, Set<Long> list);

    void deleteOrderFromUser(Long userId, Set<Long> list);

    OrderListWrapperDto getAll(OrderFilterDto filterDto);

}

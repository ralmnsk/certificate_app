package com.epam.esm.service.order;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.filter.OrderFilterDto;
import com.epam.esm.service.dto.wrapper.OrderListWrapperDto;

import java.util.Set;

public interface OrderService extends CrudService<OrderDto, Long> {
    void addOrderToUser(Long userId, Set<Long> list);

    void deleteOrderFromUser(Long userId, Set<Long> list);

    OrderListWrapperDto getAll(OrderFilterDto filterDto);

}

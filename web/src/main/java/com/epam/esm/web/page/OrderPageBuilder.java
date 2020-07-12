package com.epam.esm.web.page;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.web.assembler.OrderAssembler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class OrderPageBuilder extends AbstractPageBuilder<OrderDto, OrderService<OrderDto, Long>, OrderAssembler> {
    private final String EMPTY = "";

    public OrderPageBuilder(OrderService<OrderDto, Long> service, OrderAssembler assembler, OrderService<OrderDto, Long> orderService, OrderAssembler orderAssembler) {
        super(new HashSet<>(Arrays.asList("orders.id")), service, assembler);
    }
}
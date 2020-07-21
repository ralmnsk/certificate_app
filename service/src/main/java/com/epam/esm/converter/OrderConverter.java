package com.epam.esm.converter;

import com.epam.esm.model.Order;
import com.epam.esm.dto.OrderDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter implements Converter<OrderDto, Order> {
    private ModelMapper mapper;

    public OrderConverter(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Order toEntity(OrderDto orderDto) {
        Order order = mapper.map(orderDto, Order.class);
        return order;
    }

    @Override
    public OrderDto toDto(Order order) {
        OrderDto orderDto = mapper.map(order, OrderDto.class);
        return orderDto;
    }
}

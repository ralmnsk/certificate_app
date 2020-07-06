package com.epam.esm.service.converter;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
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
//        UserDto userDto = orderDto.getUserDto();
//        if (userDto != null) {
//            User user = mapper.map(userDto, User.class);
//            order.setUser(user);
//        }
        return order;
    }

    @Override
    public OrderDto toDto(Order order) {
        OrderDto orderDto = mapper.map(order, OrderDto.class);
//        User user = order.getUser();
//        if (user != null) {
//            UserDto userDto = mapper.map(user, UserDto.class);
//            userDto.getOrders().clear();
//            orderDto.setUserDto(userDto);
//        }
        return orderDto;
    }
}

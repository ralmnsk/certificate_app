package com.epam.esm.service.order;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.repository.jpa.OrderRepository;
import com.epam.esm.repository.jpa.UserRepository;
import com.epam.esm.service.converter.OrderConverter;
import com.epam.esm.service.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OrderServiceImpl implements OrderService<OrderDto, Long> {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private OrderConverter orderConverter;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, OrderConverter orderConverter) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderConverter = orderConverter;
    }

    @Override
    public Optional<OrderDto> save(OrderDto orderDto) {
        Optional<OrderDto> orderDtoOptional = Optional.empty();
        if (orderDto != null) {
            Order order = orderConverter.toEntity(orderDto);
            orderRepository.saveAndFlush(order);
            return get(order.getId());
        }
        return orderDtoOptional;
    }

    @Override
    public Optional<OrderDto> get(Long id) {
        Optional<OrderDto> orderDtoOptional = Optional.empty();
        Order order = orderRepository.getOne(id);
        setCorrectTime(order);
        orderDtoOptional = Optional.ofNullable(orderConverter.toDto(order));

        return orderDtoOptional;
    }


    @Override
    public Optional<OrderDto> update(OrderDto orderDto) {
        return save(orderDto);
    }

    @Override
    public boolean delete(Long id) {
        orderRepository.deleteById(id);
        return true;
    }

    @Override
    public Page<OrderDto> getAll(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OrderDto> dtoList = orders.getContent()
                .stream()
                .map(o -> orderConverter.toDto(o))
                .collect(Collectors.toList());
        return new PageImpl<OrderDto>(dtoList, pageable, dtoList.size());
    }

    @Override
    public Page<OrderDto> getAllByUserId(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.getAllByUserId(userId, pageable);
        List<OrderDto> dtoList = orders.getContent()
                .stream()
                .map(o -> orderConverter.toDto(o))
                .collect(Collectors.toList());
        return new PageImpl<OrderDto>(dtoList, pageable, dtoList.size());
    }

    @Override
    public Optional<OrderDto> createOrderInUser(Long userId, OrderDto orderDto) {
        Optional<OrderDto> orderDtoOptional = Optional.empty();
        if (orderDto.getId() != null && orderDto.getId() > 0L) {
            orderDtoOptional = get(orderDto.getId());
        } else {
            orderDtoOptional = save(orderDto);
        }

        Order order = null;
        if (orderDtoOptional.isPresent()) {
            orderDto = orderDtoOptional.get();
            order = orderConverter.toEntity(orderDto);
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent() && order != null) {
            order.setUser(userOptional.get());
            order = orderRepository.save(order);
            orderDto = orderConverter.toDto(order);
            return Optional.ofNullable(orderDto);
        }

        return orderDtoOptional;
    }

    @Override
    public Long getOrderByCertificateId(Long certId) {
        Long num = 0L;
        Optional<Order> orderOptional = orderRepository.getOrderByCertificateId(certId);
        if (orderOptional.isPresent()) {
            num = orderOptional.get().getId();
        }

        return num;
    }

    private void setCorrectTime(Order order) {
        Instant created = order.getCreated();
        if (created != null) {
            order.setCreated(Timestamp.from(created).toLocalDateTime().toInstant(ZoneOffset.UTC));
        } else {
            order.setCreated(Instant.now());
        }
    }


}

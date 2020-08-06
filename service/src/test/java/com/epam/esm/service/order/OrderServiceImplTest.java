package com.epam.esm.service.order;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.model.filter.OrderFilter;
import com.epam.esm.model.wrapper.OrderListWrapper;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.calculator.TotalCostCalculator;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.filter.OrderFilterDto;
import com.epam.esm.dto.wrapper.OrderListWrapperDto;
import com.epam.esm.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private TotalCostCalculator calculator = new TotalCostCalculator(mapper);
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private Certificate certificate;
    private OrderDto orderDto;
    private ModelMapper modelMapper;
    private User user;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        order = new Order();
        order.setDeleted(false);
        order.setTotalCost(new BigDecimal(0));
        order.setDescription("description");
        order.setCreated(Timestamp.from(Instant.now()));
        order.setId(1L);

        certificate = new Certificate();
        certificate.setDeleted(false);
        certificate.setDuration(10);
        certificate.setDescription("certificate description");
        certificate.setPrice(new BigDecimal(50));
        certificate.setName("name");
        certificate.setId(2L);
        certificate.setCreation(order.getCreated());
        order.getCertificates().add(certificate);
        modelMapper = new ModelMapper();
        orderDto = modelMapper.map(order, OrderDto.class);

        user = new User();
        user.setRole(Role.USER);
        user.setName("name");
        user.setSurname("surname");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setLogin("login");
        user.setId(1L);
        user.setDeleted(false);
        user.getOrders().add(order);
    }

    @Test
    void save() {
        when(orderRepository.save(order)).thenReturn(Optional.ofNullable(order));
        when(orderRepository.get(order.getId())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.update(order)).thenReturn(Optional.ofNullable(order));
        when(mapper.map(orderDto, Order.class)).thenReturn(order);
        orderDto = modelMapper.map(order, OrderDto.class);
        when(mapper.map(order, OrderDto.class)).thenReturn(orderDto);
        assertEquals(orderDto, orderService.save(orderDto).get());
    }

    @Test
    void get() {
        when(orderRepository.get(order.getId())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.update(order)).thenReturn(Optional.ofNullable(order));
        when(mapper.map(orderDto, Order.class)).thenReturn(order);
        orderDto = modelMapper.map(order, OrderDto.class);
        when(mapper.map(order, OrderDto.class)).thenReturn(orderDto);
        assertEquals(orderDto, orderService.get(order.getId()).get());
    }

    @Test
    void update() {
        when(orderRepository.get(order.getId())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.update(order)).thenReturn(Optional.ofNullable(order));
        when(mapper.map(orderDto, Order.class)).thenReturn(order);
        orderDto = modelMapper.map(order, OrderDto.class);
        when(mapper.map(order, OrderDto.class)).thenReturn(orderDto);
        assertEquals(orderDto, orderService.update(orderDto).get());
    }

    @Test
    void delete() {
        when(orderRepository.get(order.getId())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.update(order)).thenReturn(Optional.ofNullable(order));
        orderService.delete(1L);
        verify(orderRepository).delete(1L);
    }

    @Test
    void getAll() {
        OrderFilter filter = new OrderFilter();
        OrderFilterDto filterDto = new OrderFilterDto();
        when(mapper.map(filterDto, OrderFilter.class)).thenReturn(filter);
        OrderListWrapper wrapper = new OrderListWrapper();
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        wrapper.setFilter(filter);
        wrapper.setList(orders);

        OrderListWrapperDto wrapperDto = modelMapper.map(wrapper, OrderListWrapperDto.class);
        when(mapper.map(order, OrderDto.class)).thenReturn(orderDto);
        when(orderRepository.getAll(filter)).thenReturn(wrapper);
        OrderListWrapperDto all = orderService.getAll(filterDto);
        assertEquals(wrapperDto, all);

    }

    @Test
    void addOrderToUser() {
        when(userRepository.get(1L)).thenReturn(Optional.ofNullable(user));
        when(orderRepository.get(1L)).thenReturn(Optional.ofNullable(order));
        when(userRepository.update(user)).thenReturn(Optional.ofNullable(user));
        Set<Long> set = new HashSet();
        set.add(1L);
        orderService.addOrderToUser(1L, set);
        verify(userRepository).update(user);
        assertTrue(user.getOrders().contains(order));
    }

    @Test
    void deleteOrderFromUser() {
        when(userRepository.get(1L)).thenReturn(Optional.ofNullable(user));
        when(orderRepository.get(1L)).thenReturn(Optional.ofNullable(order));
        when(userRepository.update(user)).thenReturn(Optional.ofNullable(user));
        Set<Long> set = new HashSet();
        set.add(1L);
        orderService.addOrderToUser(1L, set);
        verify(userRepository).update(user);
    }
}
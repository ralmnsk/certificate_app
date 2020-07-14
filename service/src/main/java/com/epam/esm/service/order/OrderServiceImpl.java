package com.epam.esm.service.order;

import com.epam.esm.model.ListWrapper;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.model.filter.OrderFilter;
import com.epam.esm.model.filter.UserFilter;
import com.epam.esm.repository.crud.OrderRepository;
import com.epam.esm.repository.crud.UserRepository;
import com.epam.esm.service.calculator.TotalCostCalculator;
import com.epam.esm.service.dto.ListWrapperDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.filter.OrderFilterDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.exception.UpdateException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OrderServiceImpl implements OrderService<OrderDto, Long, OrderFilterDto> {

    private OrderRepository<Order, Long, OrderFilter> orderRepository;
    private ModelMapper mapper;
    private TotalCostCalculator calculator;
    private UserRepository<User, Long, UserFilter> userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper mapper, TotalCostCalculator calculator,
                            UserRepository<User, Long, UserFilter> userRepository) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.calculator = calculator;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<OrderDto> save(OrderDto orderDto) {
        orderDto.getCertificates().clear();
        Order order = mapper.map(orderDto, Order.class);
        order = orderRepository.save(order).orElseThrow(() -> new SaveException("OrderService: Order save exception"));
        return get(order.getId());
    }


    @Override
    public Optional<OrderDto> get(Long id) {
        Optional<OrderDto> orderDtoOptional = Optional.empty();
        Order order = orderRepository.get(id).orElseThrow(() -> new NotFoundException("OrderService: Order get exception" + id));
        calculator.calc(order);
        order = orderRepository.update(order).orElseThrow(() -> new UpdateException("OrderService: update in get operation exception"));

        OrderDto orderDto = mapper.map(order, OrderDto.class);
        orderDto.getCertificates().clear();
        orderDtoOptional = Optional.ofNullable(orderDto);

        return orderDtoOptional;
    }


    @Override
    public Optional<OrderDto> update(OrderDto orderDto) {
        long id = orderDto.getId();
        Order found = orderRepository.get(orderDto.getId()).orElseThrow(() -> new NotFoundException("OrderService: get in update operation exception" + id));

        found.setDescription(orderDto.getDescription());
        Order order = orderRepository.update(found).orElseThrow(() -> new SaveException("OrderService:Order save exception"));
        OrderDto dto = mapper.map(order, OrderDto.class);

        return Optional.ofNullable(dto);
    }

    @Override
    public boolean delete(Long id) {
        Order order = orderRepository.get(id).orElseThrow(() -> new NotFoundException("Order delete: not found exception, id:" + id));
        order.setDeleted(true);
        orderRepository.update(order).orElseThrow(() -> new UpdateException("Order update in delete operation exception"));
        return true;
    }

    @Override
    public ListWrapperDto<OrderDto, OrderFilterDto> getAll(OrderFilterDto filterDto) {
        OrderFilter filter = mapper.map(filterDto, OrderFilter.class);
        ListWrapper<Order, OrderFilter> wrapper = orderRepository.getAll(filter);
        List<Order> orders = wrapper.getList();
        List<OrderDto> dtoList = orders
                .stream()
                .map(o -> mapper.map(o, OrderDto.class))
                .collect(Collectors.toList());
        dtoList.forEach(d -> d.getCertificates().clear());

        ListWrapperDto<OrderDto, OrderFilterDto> wrapperDto = new ListWrapperDto<>();
        wrapperDto.setList(dtoList);
        filter = wrapper.getFilter();
        wrapperDto.setFilterDto(mapper.map(filter, OrderFilterDto.class));

        return wrapperDto;
    }

    @Override
    public void addOrderToUser(Long userId, Set<Long> list) {
        User user = userRepository.get(userId).orElseThrow(() -> new NotFoundException("Add Order to User: user not found: id:" + userId));
        list
                .stream()
                .map(idDto -> orderRepository.get(idDto).orElseThrow(() -> new NotFoundException("Add Order to User: Order not found: id:" + idDto)))
                .forEach(order -> user.getOrders().add(order));
        userRepository.update(user).orElseThrow(() -> new UpdateException("Add Order to User: user update exception"));
    }

    @Override
    public void deleteOrderFromUser(Long userId, Set<Long> list) {
        User user = userRepository.get(userId).orElseThrow(() -> new NotFoundException("Add Order to User: user not found: id:" + userId));
        list
                .stream()
                .map(idDto -> orderRepository.get(idDto).orElseThrow(() -> new NotFoundException("Add Order to User: Order not found: id:" + idDto)))
                .forEach(order -> user.getOrders().remove(order));
        userRepository.update(user).orElseThrow(() -> new UpdateException("Add Order to User: user update exception"));
    }

}

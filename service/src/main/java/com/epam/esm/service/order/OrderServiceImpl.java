package com.epam.esm.service.order;

import com.epam.esm.model.Filter;
import com.epam.esm.model.Order;
import com.epam.esm.repository.crud.OrderCrudRepository;
import com.epam.esm.service.calculator.TotalCostCalculator;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.exception.UpdateException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Getter
@Transactional
public class OrderServiceImpl implements OrderService<OrderDto, Long> {
    private FilterDto filterDto;
    private OrderCrudRepository orderRepository;
    private ModelMapper mapper;
    private TotalCostCalculator calculator;

    public OrderServiceImpl(OrderCrudRepository orderRepository, ModelMapper mapper, TotalCostCalculator calculator) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.calculator = calculator;
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
    public List<OrderDto> getAll(FilterDto filterDto) {
        Filter filter = mapper.map(filterDto, Filter.class);
        List<Order> orders = orderRepository.getAll(filter);
        List<OrderDto> dtoList = orders
                .stream()
                .map(o -> mapper.map(o, OrderDto.class))
                .collect(Collectors.toList());
        dtoList.forEach(d -> d.getCertificates().clear());

        filter = orderRepository.getFilter();
        this.filterDto = mapper.map(filter, FilterDto.class);

        return dtoList;
    }

//    @Override
//    public Page<OrderDto> getAllByUserId(Long userId, Pageable pageable) {
//        Page<Order> orders = orderRepository.getAllByUserId(userId, pageable);
//        List<OrderDto> dtoList = orders.getContent()
//                .stream()
//                .map(o -> mapper.map(o, OrderDto.class))
//                .collect(Collectors.toList());
//        dtoList.forEach(d -> d.getCertificates().clear());
//        return new PageImpl<OrderDto>(dtoList, pageable, dtoList.size());
//    }

//    @Override
//    public Optional<OrderDto> createOrderInUser(Long userId, OrderDto orderDto) {
//        userRepository.get(userId).orElseThrow(() -> new NotFoundException(userId));
//        orderDto = save(orderDto).orElseThrow(() -> new SaveException("Order save exception"));
//        orderRepository.addOrderToUser(userId, orderDto.getId());
//
//        return Optional.of(orderDto);
//    }
//
//
//    @Override
//    public Optional<OrderDto> addCertificateToOrder(Long orderId, List<IdDto> listIdDto) {
//        Order order = orderRepository.get(orderId).orElseThrow(() -> new NotFoundException(orderId));
//
//        if (listIdDto != null && !listIdDto.isEmpty()) {
//            List<Certificate> certificates = new ArrayList<>();
//            CertificateCrudRepository certificateCrudRepository = certificateRepository;
//            for (IdDto idDto : listIdDto) {
//                Long id = idDto.getId();
//                Optional<Certificate> certificate = certificateCrudRepository.get(id);
//                if (certificate.isPresent()) {
//                    Certificate certificate1 = certificate.get();
//                    certificates.add(certificate1);
//                }
//            }
//            order.getCertificates().addAll(certificates);
//        }
//        calculator.calc(order);
//        orderRepository.save(order);
//        return Optional.ofNullable(mapper.map(order, OrderDto.class));
//    }

//    @Override
//    public Optional<OrderDto> removeCertificateFromOrder(Long orderId, List<IdDto> listIdDto) {
//        Order order = orderRepository.get(orderId).orElseThrow(() -> new NotFoundException(orderId));
//
//        if (listIdDto != null && !listIdDto.isEmpty() && !order.getCertificates().isEmpty()) {
//            Set<Certificate> certificates = new HashSet<>(order.getCertificates());
//            listIdDto.stream()
//                    .map(IdDto::getId)
//                    .forEach(i -> certificates.stream()
//                            .filter(c -> c.getId().equals(i))
//                            .forEach(c -> order.getCertificates().remove(c)));
//        }
//        calculator.calc(order);
//        orderRepository.save(order);
//        return Optional.ofNullable(mapper.map(order, OrderDto.class));
//    }

}

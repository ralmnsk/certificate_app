package com.epam.esm.web.assembler;

import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.web.controller.OrderController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements Assembler<Long, OrderDto> {
    private OrderService<OrderDto, Long> orderService;

    public OrderAssembler(OrderService<OrderDto, Long> orderService) {
        this.orderService = orderService;
    }

    public OrderDto assemble(Long orderId, OrderDto orderDto) {
        Link linkSelfOrder = linkTo(methodOn(OrderController.class).get(orderDto.getId())).withSelfRel();
        orderDto.add(linkSelfOrder);

        return orderDto;
    }


    public CollectionModel<OrderDto> toCollectionModel(FilterDto filter) {
        List<OrderDto> orders = orderService.getAll(filter);
        orders.forEach(o -> {
            Link selfLink = linkTo(methodOn(OrderController.class).get(o.getId())).withSelfRel();
            o.add(selfLink);
        });

        Link link = linkTo(methodOn(OrderController.class)
                .getAll(filter.getUserSurname(), filter.getUserName(), filter.getCertificateName(), filter.getPage(), filter.getSize(), filter.getSortParams())).withRel("orders");
        CollectionModel<OrderDto> collectionModel = CollectionModel.of(orders, link);

        return collectionModel;
    }
}

package com.epam.esm.web.assembler;

import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.controller.UserController;
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

        CollectionModel<OrderDto> collectionModel = CollectionModel.of(orders);
        if (filter.getUserId() != null && filter.getUserId() > 0) {
            Link link = linkTo(methodOn(UserController.class)
                    .getAllOrdersByUserId(filter.getUserSurname(),
                            filter.getUserName(),
                            filter.getPage(), filter.getSize(), filter.getSortParams(),
                            filter.getUserId())).withRel("user id:" + filter.getUserId() + " orders");
            collectionModel.add(link);
            filter = orderService.getFilterDto();
            addNextPrevious(collectionModel, filter);
        } else {
            Link link = linkTo(methodOn(OrderController.class)
                    .getAll(filter.getUserSurname(), filter.getUserName(), filter.getCertificateName(), filter.getPage(), filter.getSize(), filter.getSortParams())).withRel("orders");
            collectionModel.add(link);
        }

        return collectionModel;
    }

    private void addNextPrevious(CollectionModel<OrderDto> collectionModel, FilterDto filter) {
        int page = filter.getPage();

        if (page > 0 && page <= filter.getTotalPages()) {
            Link link = linkTo(methodOn(UserController.class)
                    .getAllOrdersByUserId(filter.getUserSurname(),
                            filter.getUserName(),
                            filter.getPage() - 1,
                            filter.getSize(),
                            filter.getSortParams(),
                            filter.getUserId()
                    )).withRel("user id:" + filter.getUserId() + " orders previous page");
            collectionModel.add(link);
        }

        if (page >= 0 && page < filter.getTotalPages()) {
            Link link = linkTo(methodOn(UserController.class)
                    .getAllOrdersByUserId(filter.getUserSurname(),
                            filter.getUserName(),
                            filter.getPage() + 1,
                            filter.getSize(),
                            filter.getSortParams(),
                            filter.getUserId()
                    )).withRel("user id:" + filter.getUserId() + " orders next page");
            collectionModel.add(link);
        }
    }
}

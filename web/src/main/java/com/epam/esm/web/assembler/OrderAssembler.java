package com.epam.esm.web.assembler;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.controller.UserController;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.web.controller.ControllerConstants.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements Assembler<Long, OrderDto> {
    private UserService<UserDto, Long> userService;

    public OrderAssembler(UserService<UserDto, Long> userService) {
        this.userService = userService;
    }

    @Override
    public OrderDto assemble(Long orderId, OrderDto orderDto) {
        Link linkSelfOrder = linkTo(methodOn(OrderController.class).get(orderDto.getId())).withSelfRel();
        Link linkToCerts = linkTo(methodOn(OrderController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_CERTS)), orderId)).withRel("certificates");
        Long userId = orderDto.getUserDto().getId();
        Link linkToAllUserOrders = linkTo(methodOn(UserController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE), userId)).withRel("orders");
        orderDto.add(linkSelfOrder, linkToCerts, linkToAllUserOrders);

        return orderDto;
    }

    @Override   //user orders users/ order certificates orders
    public CollectionModel<OrderDto> toCollectionModel(Long userId, List<OrderDto> orders, Pageable pageable) {
        orders.forEach(o -> {
            Link selfLink = linkTo(methodOn(OrderController.class).get(o.getId())).withSelfRel();
            o.add(selfLink);
        });

        if ((userId == null || userId.equals(PARAM_NOT_USED)) && !orders.isEmpty()) {
            Long orderId = orders.get(0).getId();
            Long foundId = userService.getUserIdByOrderId(orderId);
            if (foundId != null) {
                userId = foundId;
            }
        }

        Link linkOrders = linkTo(methodOn(UserController.class).getAll(pageable, userId)).withSelfRel();
        Link linkUser = linkTo(methodOn(UserController.class).get(userId)).withRel("user");

        return CollectionModel.of(orders, linkOrders, linkUser);
    }
}

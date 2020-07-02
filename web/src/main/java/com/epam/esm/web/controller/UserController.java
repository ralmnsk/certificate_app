package com.epam.esm.web.controller;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.service.user.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.epam.esm.web.controller.ControllerConstants.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService<UserDto, Long> userService;
    private OrderService<OrderDto, Long> orderService;

    public UserController(UserService<UserDto, Long> userService, OrderService<OrderDto, Long> orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        userDto = userService.save(userDto).orElseThrow(() -> new SaveException("User save exception"));
        Link link = linkTo(methodOn(UserController.class).get(userDto.getId())).withSelfRel();
        Link linkToAll = linkTo(methodOn(UserController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE))).withRel("users");
        userDto.add(link, linkToAll);

        return userDto;
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        UserDto userDto = userService.get(id).orElseThrow(() -> new NotFoundException(id));
//        if (!userDto.getOrders().isEmpty()) {
//            userDto.getOrders().stream().forEach(o -> {
//                Link linkOrder = linkTo(methodOn(OrderController.class).get(o.getId())).withSelfRel();
//                o.add(linkOrder);
//            });
//        }
        Link linkSelf = linkTo(methodOn(UserController.class).get(userDto.getId())).withSelfRel();
        Link linkToUserOrders = linkTo(methodOn(UserController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_ORDERS)), id)).withRel("orders");
        Link linkToAll = linkTo(methodOn(UserController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE))).withRel("users");
        userDto.add(linkSelf, linkToUserOrders, linkToAll);

        return userDto;
    }


    @PutMapping("/{id}")
    public UserDto update(@Valid @RequestBody UserDto userDto, @PathVariable Long id) {
        userDto.setId(id);
        userDto = userService.update(userDto).orElseThrow(() -> new NotFoundException(id));
        Link linkSelf = linkTo(methodOn(UserController.class).get(userDto.getId())).withSelfRel();
        Link linkOrders = linkTo(methodOn(UserController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_ORDERS)))).withRel("orders");
        Link linkToAll = linkTo(methodOn(UserController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE))).withRel("users");
        userDto.add(linkSelf, linkOrders, linkToAll);

        return userDto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<UserDto> getAll(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        List<UserDto> users = userService.getAll(pageable).getContent();

        users.stream().forEach(u -> {
            Link link = linkTo(methodOn(UserController.class).get(u.getId())).withSelfRel();
            Link linkOrders = linkTo(methodOn(UserController.class).getAll(pageable, u.getId())).withRel("orders");
            u.add(link);
            u.add(linkOrders);
        });
        Link linkUsers = linkTo(methodOn(UserController.class).getAll(pageable)).withRel("users");


        return CollectionModel.of(users, linkUsers);
    }

    @GetMapping("/{userId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<OrderDto> getAll(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE) Pageable pageable, @PathVariable Long userId) {
        List<OrderDto> orders = orderService.getAllByUserId(userId, pageable).getContent();
        orders.stream().forEach(o -> {
            Link selfLink = linkTo(methodOn(OrderController.class).get(o.getId())).withSelfRel();
            o.add(selfLink);
        });

        Link link = linkTo(methodOn(UserController.class).getAll(pageable, userId)).withSelfRel();

        return CollectionModel.of(orders, link);
    }

    @PostMapping("/{userId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto createOrderInUser(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT_ORDERS) Pageable pageable, @PathVariable Long userId, @Valid @RequestBody OrderDto orderDto) {
        orderDto = orderService.createOrderInUser(userId, orderDto).orElseThrow(() -> new SaveException("Create Order in User Exception"));
        Link selfLink = linkTo(methodOn(OrderController.class).get(orderDto.getId())).withSelfRel();
        orderDto.add(selfLink);
        Link userLink = linkTo(methodOn(UserController.class).get(userId)).withRel("user");
        orderDto.add(userLink);
        Link ordersLink = linkTo(methodOn(OrderController.class).getAll(pageable, userId)).withRel("orders");
        orderDto.add(ordersLink);
        return orderDto;
    }


}

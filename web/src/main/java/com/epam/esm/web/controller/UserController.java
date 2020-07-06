package com.epam.esm.web.controller;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.assembler.OrderAssembler;
import com.epam.esm.web.assembler.UserAssembler;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.epam.esm.web.controller.ControllerConstants.*;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService<UserDto, Long> userService;
    private OrderService<OrderDto, Long> orderService;
    private UserAssembler userAssembler;
    private OrderAssembler orderAssembler;

    public UserController(UserService<UserDto, Long> userService, OrderService<OrderDto, Long> orderService, UserAssembler userAssembler, OrderAssembler orderAssembler) {
        this.userService = userService;
        this.orderService = orderService;
        this.userAssembler = userAssembler;
        this.orderAssembler = orderAssembler;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        userDto = userService.save(userDto).orElseThrow(() -> new SaveException("User save exception"));
        return userAssembler.assemble(userDto.getId(), userDto);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        UserDto userDto = userService.get(id).orElseThrow(() -> new NotFoundException(id));
        return userAssembler.assemble(id, userDto);
    }


    @PutMapping("/{id}")
    public UserDto update(@Valid @RequestBody UserDto userDto, @PathVariable Long id) {
        userDto.setId(id);
        userDto = userService.update(userDto).orElseThrow(() -> new NotFoundException(id));
        return userAssembler.assemble(id, userDto);
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

        return userAssembler.toCollectionModel(PARAM_NOT_USED, users, pageable);
    }

    @GetMapping("/{userId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<OrderDto> getAll(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE) Pageable pageable, @PathVariable Long userId) {
        List<OrderDto> orders = orderService.getAllByUserId(userId, pageable).getContent();

        return orderAssembler.toCollectionModel(userId, orders, pageable);
    }

    @PostMapping("/{userId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto createOrderInUser(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT_ORDERS) Pageable pageable, @PathVariable Long userId, /*@Valid*/ @RequestBody OrderDto orderDto) {
        orderDto = orderService.createOrderInUser(userId, orderDto).orElseThrow(() -> new SaveException("Create Order in User Exception"));

        return orderAssembler.assemble(orderDto.getId(), orderDto);
    }


}

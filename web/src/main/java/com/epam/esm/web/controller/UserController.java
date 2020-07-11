package com.epam.esm.web.controller;

import com.epam.esm.service.dto.*;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.assembler.UserAssembler;
import com.epam.esm.web.page.UserPageBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private OrderService<OrderDto, Long> orderService;
    private UserService<UserDto, Long> userService;
    private UserAssembler userAssembler;
    private UserPageBuilder userPageBuilder;
    private ModelMapper mapper;

    public UserController(OrderService<OrderDto, Long> orderService, UserService<UserDto, Long> userService, UserAssembler userAssembler, UserPageBuilder userPageBuilder, ModelMapper mapper) {
        this.orderService = orderService;
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.userPageBuilder = userPageBuilder;
        this.mapper = mapper;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDtoSave userDtoSave) {
        UserDto userDto = mapper.map(userDtoSave, UserDto.class);
        userDto = userService.save(userDto).orElseThrow(() -> new SaveException("User save exception"));
        return userAssembler.assemble(userDto.getId(), userDto);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        UserDto userDto = userService.get(id).orElseThrow(() -> new NotFoundException(id));
        return userAssembler.assemble(id, userDto);
    }


    @PutMapping("/{id}")
    public UserDto update(@Valid @RequestBody UserDtoSave userDtoSave, @PathVariable Long id) {
        userDtoSave.setId(id);
        UserDto userDto = mapper.map(userDtoSave, UserDto.class);
        userDto = userService.update(userDto).orElseThrow(() -> new NotFoundException(id));
        return userAssembler.assemble(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (userService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<UserDto> getAll(

            @RequestParam(value = "surname", defaultValue = "")
            @Size(max = 16, message = "surname should be 0-16 characters") String surname,

            @RequestParam(value = "name", defaultValue = "")
            @Size(max = 16, message = "name should be 0-16 characters") String name,

            @RequestParam(required = false) List<String> sort
    ) {
        FilterDto filterDto = new FilterDto();
        filterDto.setUserName(name);
        filterDto.setUserSurname(surname);

        return userPageBuilder.build(filterDto);
    }

//    @GetMapping("/{userId}/orders")
//    @ResponseStatus(HttpStatus.OK)
//    public CustomPageDto<OrderDto, Long> getAllOrdersByUserId(
//            @RequestParam(value = "tagName", defaultValue = "")
//            @Size(max = 16, message = "tagName should be 0-16 characters") String tagName,
//
//            @RequestParam(value = "name", defaultValue = "")
//            @Size(max = 16, message = "name should be 0-16 characters") String name,
//
//            @RequestParam(value = "page", defaultValue = "0")
//            @Min(0)
//            @Max(10000000) int page,
//
//            @RequestParam(value = "size", defaultValue = "1")
//            @Min(1)
//            @Max(100) int size,
//
//            @RequestParam(required = false) List<String> sort,
//            @PathVariable Long userId) {
//
//        FilterDto filterDto = new FilterDto();
//        filterDto.setUserId(userId);
//        filterDto.setTagName(tagName);
//        filterDto.setName(name);
//        filterDto.setPage(page);
//        filterDto.setSize(size);
//        filterDto.setSortParams(sort);
//        CustomPageDto<OrderDto, Long> build = orderPageBuilder.build(filterDto);
////        List<OrderDto> orders = orderService.getAllByUserId(userId, filterDto).getContent();
////
////        return orderAssembler.toCollectionModel(userId, orders, pageable);
//        return build;
//    }
//
//    @PostMapping("/{userId}/orders")
//    @ResponseStatus(HttpStatus.OK)
//    public OrderDto createOrderInUser(@PathVariable Long userId, @Valid @RequestBody OrderDto orderDto) {
//        orderDto = orderService.createOrderInUser(userId, orderDto).orElseThrow(() -> new SaveException("Create Order in User Exception"));
//
//        return orderAssembler.assemble(orderDto.getId(), orderDto);
//    }


}

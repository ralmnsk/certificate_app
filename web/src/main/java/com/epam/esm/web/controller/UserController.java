package com.epam.esm.web.controller;

import com.epam.esm.service.dto.CustomPageDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.UserDtoSave;
import com.epam.esm.service.dto.filter.OrderFilterDto;
import com.epam.esm.service.dto.filter.UserFilterDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.assembler.OrderAssembler;
import com.epam.esm.web.assembler.UserAssembler;
import com.epam.esm.web.page.OrderPageBuilder;
import com.epam.esm.web.page.UserPageBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private OrderPageBuilder orderPageBuilder;
    private UserService<UserDto, Long, UserFilterDto> userService;
    private UserAssembler userAssembler;
    private UserPageBuilder userPageBuilder;
    private ModelMapper mapper;
    private OrderService<OrderDto, Long, OrderFilterDto> orderService;
    private OrderAssembler orderAssembler;

    public UserController(OrderPageBuilder orderPageBuilder, UserService<UserDto, Long, UserFilterDto> userService, UserAssembler userAssembler, UserPageBuilder userPageBuilder, ModelMapper mapper, OrderService<OrderDto, Long, OrderFilterDto> orderService, OrderAssembler orderAssembler) {
        this.orderPageBuilder = orderPageBuilder;
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.userPageBuilder = userPageBuilder;
        this.mapper = mapper;
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
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


            @RequestParam(value = "page", defaultValue = "0")
            @Min(0)
            @Max(10000000) int page,

            @RequestParam(value = "size", defaultValue = "5")
            @Min(1)
            @Max(100) int size,

            @RequestParam(required = false) List<String> sort
    ) {
        UserFilterDto filterDto = new UserFilterDto();
        filterDto.setUserSurname(surname);
        filterDto.setUserName(name);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSortParams(sort);

        return userPageBuilder.build(filterDto);
    }

    @GetMapping("/{userId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<OrderDto> getAllOrdersByUserId(
            @RequestParam(value = "surname", defaultValue = "")
            @Size(max = 16, message = "surname should be 0-16 characters") String surname,

            @RequestParam(value = "userName", defaultValue = "")
            @Size(max = 16, message = "Username should be 0-16 characters") String userName,


            @RequestParam(value = "page", defaultValue = "0")
            @Min(0)
            @Max(10000000) int page,

            @RequestParam(value = "size", defaultValue = "5")
            @Min(1)
            @Max(100) int size,

            @RequestParam(required = false) List<String> sort,

            @PathVariable Long userId) {


        OrderFilterDto filterDto = new OrderFilterDto();
        filterDto.setUserSurname(surname);
        filterDto.setUserName(userName);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSortParams(sort);
        filterDto.setUserId(userId);

        return orderPageBuilder.build(filterDto);

    }

    @PutMapping("/{userId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<OrderDto> addOrderToUser(@PathVariable Long userId, @Valid @RequestBody Set<Long> set) {
        orderService.addOrderToUser(userId, set);

        OrderFilterDto filterDto = new OrderFilterDto();
        filterDto.setUserId(userId);
        return orderPageBuilder.build(filterDto);
    }

    @DeleteMapping("/{userId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<OrderDto> deleteOrderFromUser(@PathVariable Long userId, @Valid @RequestBody Set<Long> set) {
        orderService.deleteOrderFromUser(userId, set);

        OrderFilterDto filterDto = new OrderFilterDto();
        filterDto.setUserId(userId);
        return orderPageBuilder.build(filterDto);
    }


}

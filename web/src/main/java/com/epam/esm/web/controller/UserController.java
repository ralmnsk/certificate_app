package com.epam.esm.web.controller;

import com.epam.esm.dto.CustomPageDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserUpdateDto;
import com.epam.esm.dto.filter.OrderFilterDto;
import com.epam.esm.dto.filter.UserFilterDto;
import com.epam.esm.repository.exception.NotFoundException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.web.assembler.UserAssembler;
import com.epam.esm.web.page.OrderPageBuilder;
import com.epam.esm.web.page.UserPageBuilder;
import com.epam.esm.web.security.config.WebSecurity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private OrderPageBuilder orderPageBuilder;
    private UserService userService;
    private UserAssembler userAssembler;
    private UserPageBuilder userPageBuilder;
    private OrderService orderService;
    private WebSecurity webSecurity;

    public UserController(OrderPageBuilder orderPageBuilder, UserService userService,
                          UserAssembler userAssembler, UserPageBuilder userPageBuilder,
                          OrderService orderService, WebSecurity webSecurity) {
        this.orderPageBuilder = orderPageBuilder;
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.userPageBuilder = userPageBuilder;
        this.orderService = orderService;
        this.webSecurity = webSecurity;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto get(@PathVariable Long id, Authentication authentication) {
        String login = authentication.getName();
        webSecurity.checkUserId(login, id);
        UserDto userDto = userService.get(id).orElseThrow(() -> new NotFoundException(id));
        return userAssembler.assemble(id, userDto, authentication);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@Valid @RequestBody UserUpdateDto userUpdateDto, @PathVariable Long id,
                          Authentication authentication) {
        String login = authentication.getName();
        webSecurity.checkUserId(login, id);
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setSurname(userUpdateDto.getSurname());
        userDto.setName(userUpdateDto.getName());
        userDto.setPassword(userUpdateDto.getPassword());

        userDto = userService.update(userDto).orElseThrow(() -> new NotFoundException(id));
        return userAssembler.assemble(id, userDto, authentication);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Principal principal) {
        webSecurity.checkUserId(principal, id);
        userService.delete(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<?> getAll(

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

            @RequestParam(required = false) List<String> sort,
            Principal principal
    ) {
        UserFilterDto filterDto = new UserFilterDto();
        filterDto.setUserSurname(surname);
        filterDto.setUserName(name);
        String login = principal.getName();
        UserDto byLogin = userService.findByLogin(login);
        if (byLogin != null) {
            filterDto.setUserId(byLogin.getId());
        }
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

            @PathVariable Long userId,

            Principal principal
    ) {
        webSecurity.checkUserId(principal, userId);

        OrderFilterDto filterDto = new OrderFilterDto();
        filterDto.setUserSurname(surname);
        filterDto.setUserName(userName);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSortParams(sort);
        filterDto.setUserId(userId);

        return orderPageBuilder.build(filterDto);
    }
}

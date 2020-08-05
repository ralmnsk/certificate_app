package com.epam.esm.web.controller;

import com.epam.esm.dto.CustomPageDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserUpdateDto;
import com.epam.esm.dto.filter.OrderFilterDto;
import com.epam.esm.dto.filter.UserFilterDto;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.exception.NotFoundException;
import com.epam.esm.repository.exception.UpdateException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.web.assembler.UserAssembler;
import com.epam.esm.web.page.OrderPageBuilder;
import com.epam.esm.web.page.UserPageBuilder;
import com.epam.esm.web.security.config.WebSecurity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private OrderPageBuilder orderPageBuilder;
    private UserService userService;
    private UserAssembler userAssembler;
    private UserPageBuilder userPageBuilder;
    private OrderService orderService;
    private ObjectMapper objectMapper;
    private WebSecurity webSecurity;
    private BCryptPasswordEncoder encoder;

    public UserController(OrderPageBuilder orderPageBuilder, UserService userService, UserAssembler userAssembler, UserPageBuilder userPageBuilder, OrderService orderService, ObjectMapper objectMapper, WebSecurity webSecurity, BCryptPasswordEncoder encoder) {
        this.orderPageBuilder = orderPageBuilder;
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.userPageBuilder = userPageBuilder;
        this.orderService = orderService;
        this.objectMapper = objectMapper;
        this.webSecurity = webSecurity;
        this.encoder = encoder;
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
            @Min(value = 0, message = "page must be 0-10000000")
            @Max(value = 10000000, message = "page must be 0-10000000") int page,

            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "size must be 1-100")
            @Max(value = 100, message = "size must be 1-100") int size,

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
            @Min(value = 0, message = "page must be 1-100")
            @Max(value = 10000000, message = "page must be 1-100") int page,

            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "size must be 1-100")
            @Max(value = 100, message = "size must be 1-100") int size,

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

    @PatchMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable Long id, @RequestBody JsonPatch patch, Authentication authentication) {
        String login = authentication.getName();
        webSecurity.checkUserId(login, id);
        UserDto userDto = userService.get(id).orElseThrow(() -> new NotFoundException(id));
        UserUpdateDto userUpdateDtoPatched = null;
        try {
            userUpdateDtoPatched = applyPatchToUser(patch);
        } catch (JsonPatchException e) {
            log.warn("User patch exception:{}", e.getMessage());
            throw new UpdateException("User patch processing exception:" + e.getMessage());
        } catch (JsonProcessingException e) {
            log.warn("User patch processing exception:{}", e.getMessage());
            throw new UpdateException("User patch processing exception:" + e.getMessage());
        }
        userDto = patchToDto(userDto, userUpdateDtoPatched);
        userDto = userService.update(userDto).orElseThrow(() -> new UpdateException(id));

        return userAssembler.assemble(id, userDto, authentication);
    }

    private UserDto patchToDto(UserDto dto, UserUpdateDto patched) {
        Map<String, String> errors = new HashMap<>();

        if (patched.getName() != null) {
            boolean matches = patched.getName().matches("([А-Яа-яa-zA-Z0-9- .!&?#,;$]){2,64}");
            if (matches) {
                dto.setName(patched.getName());
            } else {
                errors.put("name:", "Name must be between 2 and 64 characters. Use these letters:[А-Яа-яa-zA-Z0-9- .!&?#,;$].");
            }
        }

        if (patched.getSurname() != null) {
            boolean matches = patched.getSurname().matches("([А-Яа-яa-zA-Z0-9- .!&?#,;$]){2,64}");
            if (matches) {
                dto.setSurname(patched.getSurname());
            } else {
                errors.put("surname:", "Surname must be between 2 and 64 characters. Use these letters:[А-Яа-яa-zA-Z0-9- .!&?#,;$].");
            }
        }

        if (patched.getPassword() != null) {
            boolean matches = patched.getPassword().matches("([А-Яа-яa-zA-Z0-9- .!&?#,;$]){2,64}");
            if (matches) {
                dto.setPassword(patched.getPassword());
            } else {
                errors.put("password:", "Password must be between 2 and 64 characters. Use these letters:[А-Яа-яa-zA-Z0-9- .!&?#,;$].");
            }
        }

        if (!errors.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            errors.forEach((k, v) -> {
                builder.append("Field ").append(k).append(v).append("  ");
            });
            log.error(builder.toString());
            ValidationException validationException = new ValidationException(builder.toString());
            validationException.getFieldsException().putAll(errors);
            errors.clear();
            throw validationException;
        }

        return dto;
    }

    private UserUpdateDto applyPatchToUser(JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        UserUpdateDto newUserDto = new UserUpdateDto();
        JsonNode patched = patch.apply(objectMapper.convertValue(newUserDto, JsonNode.class));
        return objectMapper.treeToValue(patched, UserUpdateDto.class);
    }
}

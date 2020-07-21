package com.epam.esm.service;


import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.filter.UserFilterDto;
import com.epam.esm.dto.wrapper.UserListWrapperDto;

import java.util.Optional;

public interface UserService extends CrudService<UserDto, Long> {

    UserDto findByLogin(String login);

    UserListWrapperDto getAll(UserFilterDto filterDto);

    Optional<UserDto> getUserByOrderId(Long orderId);
}

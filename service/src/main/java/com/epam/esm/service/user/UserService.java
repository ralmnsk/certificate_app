package com.epam.esm.service.user;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.filter.UserFilterDto;
import com.epam.esm.service.dto.wrapper.UserListWrapperDto;

import java.util.Optional;

public interface UserService extends CrudService<UserDto, Long> {

    UserDto findByLogin(String login);

    UserListWrapperDto getAll(UserFilterDto filterDto);

    Optional<UserDto> getUserByOrderId(Long orderId);
}

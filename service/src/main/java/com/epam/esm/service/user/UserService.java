package com.epam.esm.service.user;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.ListWrapperDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.filter.AbstractFilterDto;

public interface UserService<T, E, F extends AbstractFilterDto> extends CrudService<T, E> {

    UserDto findByLogin(String login);

    ListWrapperDto<T, F> getAll(F filterDto);
}

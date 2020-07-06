package com.epam.esm.service.converter;

import com.epam.esm.model.User;
import com.epam.esm.service.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<UserDto, User> {
    private ModelMapper mapper;

    public UserConverter(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public User toEntity(UserDto userDto) {
        return mapper.map(userDto, User.class);
    }

    @Override
    public UserDto toDto(User user) {
        UserDto userDto = mapper.map(user, UserDto.class);
        return userDto;
    }
}

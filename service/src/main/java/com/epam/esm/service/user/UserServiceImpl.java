package com.epam.esm.service.user;

import com.epam.esm.model.User;
import com.epam.esm.repository.jpa.UserRepository;
import com.epam.esm.service.converter.UserConverter;
import com.epam.esm.service.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService<UserDto, Long> {
    private UserRepository userRepository;
    private UserConverter userConverter;

    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public Optional<UserDto> save(UserDto userDto) {
        Optional<UserDto> userDtoOptional = Optional.empty();
        if (userDto != null) {
            User user = userConverter.toEntity(userDto);
            User save = userRepository.save(user);
            userDtoOptional = Optional.ofNullable(userConverter.toDto(save));
        }
        return userDtoOptional;
    }

    @Override
    public Optional<UserDto> get(Long id) {
        Optional<UserDto> userDtoOptional = Optional.empty();
        User user = userRepository.getOne(id);
        if (user != null) {
            userDtoOptional = Optional.ofNullable(userConverter.toDto(user));
        }
        return userDtoOptional;
    }

    @Override
    public Optional<UserDto> update(UserDto userDto) {
        Optional<UserDto> userDtoOptional = Optional.empty();
        if (userDto != null) {
            User user = userConverter.toEntity(userDto);
            User save = userRepository.saveAndFlush(user);
            userDtoOptional = Optional.ofNullable(userConverter.toDto(save));
        }
        return userDtoOptional;
    }

    @Override
    public boolean delete(Long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public Page<UserDto> getAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<UserDto> dtoList = users.getContent()
                .stream()
                .map(u -> userConverter.toDto(u))
                .collect(Collectors.toList());
        return new PageImpl<UserDto>(dtoList, pageable, dtoList.size());
    }
}

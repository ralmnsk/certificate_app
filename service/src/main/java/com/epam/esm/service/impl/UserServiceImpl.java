package com.epam.esm.service.impl;

import com.epam.esm.model.User;
import com.epam.esm.model.filter.UserFilter;
import com.epam.esm.model.wrapper.UserListWrapper;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.filter.UserFilterDto;
import com.epam.esm.dto.wrapper.UserListWrapperDto;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.SaveException;
import com.epam.esm.exception.UpdateException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@Getter
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper mapper;
    private OrderRepository orderRepository;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<UserDto> save(UserDto userDto) { //save user without orders
        userDto.getOrders().clear();
        User user = mapper.map(userDto, User.class);
        try {
            user = userRepository.save(user).orElseThrow(() -> new SaveException("UserService: user save exception"));
        } catch (DataIntegrityViolationException e) {
            throw new SaveException("User already exists exception");
        }

        return get(user.getId());
    }

    @Override
    public Optional<UserDto> get(Long id) {
        User user = userRepository.get(id).orElseThrow(() -> new NotFoundException(id));
        if (user.getDeleted()) {
            return Optional.empty();
        }
        UserDto userDto = mapper.map(user, UserDto.class);
        userDto.getOrders().clear();
        return Optional.ofNullable(userDto);
    }

    @Override
    public Optional<UserDto> update(UserDto userDto) {//update user without orders
        long id = userDto.getId();
        User found = userRepository.get(userDto.getId()).orElseThrow(() -> new NotFoundException("UserService: user not found in update operation exception, id:" + id));
        if (found.getDeleted()) {
            throw new NotFoundException("UserService: user not found in update operation exception, id:" + id);
        }
        found.setSurname(userDto.getSurname());
        found.setName(userDto.getName());
        found.setPassword((new BCryptPasswordEncoder()).encode(userDto.getPassword()));
//        found.setRole(userDto.getRole()); //to delete

        User user = userRepository.update(found).orElseThrow(() -> new UpdateException("UserService update: User update exception"));
        UserDto dto = mapper.map(user, UserDto.class);

        return Optional.ofNullable(dto);
    }

    @Override
    public boolean delete(Long id) {
        User user = userRepository.get(id).orElseThrow(() -> new NotFoundException("User delete: not found exception, id:" + id));
        user.setDeleted(true);
        user.getOrders().forEach(order -> {
            order.setDeleted(true);
            orderRepository.update(order);
        });
        userRepository.update(user).orElseThrow(() -> new UpdateException("Certificate update in delete operation exception"));
        return true;
    }

    @Override
    public UserListWrapperDto getAll(UserFilterDto userFilterDto) {
        UserFilter userFilter = mapper.map(userFilterDto, UserFilter.class);
        UserListWrapper wrapper = userRepository.getAll(userFilter);
        List<UserDto> dtoList = new ArrayList<>();
        List<User> users = wrapper.getList();
        for (User c : users) {
            UserDto d = mapper.map(c, UserDto.class);
            d.getOrders().clear();
            dtoList.add(d);
        }

        UserListWrapperDto wrapperDto = new UserListWrapperDto();
        wrapperDto.setList(dtoList);
        UserFilter filter = wrapper.getFilter();
        wrapperDto.setFilterDto(mapper.map(filter, UserFilterDto.class));

        return wrapperDto;
    }

    @Override
    public Optional<UserDto> getUserByOrderId(Long orderId) {
        Optional<User> userByOrderId = userRepository.getUserByOrderId(orderId);
        if (userByOrderId.isPresent()) {
            User user = userByOrderId.get();
            UserDto userDto = mapper.map(user, UserDto.class);

            return Optional.ofNullable(userDto);
        }
        return Optional.empty();
    }

    @Override
    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new NotFoundException("User login not found exception:");
        }

        return mapper.map(user, UserDto.class);
    }


}

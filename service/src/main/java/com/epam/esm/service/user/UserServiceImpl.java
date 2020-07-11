package com.epam.esm.service.user;

import com.epam.esm.model.Filter;
import com.epam.esm.model.User;
import com.epam.esm.repository.crud.UserCrudRepository;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.exception.UpdateException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@Getter
public class UserServiceImpl implements UserService<UserDto, Long> {
    private FilterDto filterDto;
    private UserCrudRepository userRepository;
    private ModelMapper mapper;

    public UserServiceImpl(UserCrudRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
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
        UserDto userDto = mapper.map(user, UserDto.class);
        return Optional.ofNullable(userDto);
    }

    @Override
    public Optional<UserDto> update(UserDto userDto) {//update user without orders
        long id = userDto.getId();
        User found = userRepository.get(userDto.getId()).orElseThrow(() -> new NotFoundException("UserService: user get in update operation exception, id:" + id));

        found.setSurname(userDto.getSurname());
        found.setName(userDto.getName());
        found.setPassword(userDto.getPassword());
        found.setRole(userDto.getRole());
        User user = userRepository.update(found).orElseThrow(() -> new UpdateException("UserService update: User update exception"));
        UserDto dto = mapper.map(user, UserDto.class);

        return Optional.ofNullable(dto);
    }

    @Override
    public boolean delete(Long id) {
        User user = userRepository.get(id).orElseThrow(() -> new NotFoundException("User delete: not found exception, id:" + id));
        user.setDeleted(true);
        userRepository.update(user).orElseThrow(() -> new UpdateException("Certificate update in delete operation exception"));
        return true;
    }

    @Override
    public List<UserDto> getAll(FilterDto filterDto) {
        Filter filter = mapper.map(filterDto, Filter.class);
        List<User> users = userRepository.getAll(filter);
        List<UserDto> dtoList = new ArrayList<>();
        for (User c : users) {
            UserDto d = mapper.map(c, UserDto.class);
            d.getOrders().clear();
            dtoList.add(d);
        }

        filter = userRepository.getFilter();
        this.filterDto = mapper.map(filter, FilterDto.class);

        return dtoList;
    }

//    @Override
//    public Long getUserIdByOrderId(Long orderId) {
//        Long num = 0L;
//        Optional<User> orderOptional = userRepository.getUserIdByOrderId(orderId);
//        if (orderOptional.isPresent()) {
//            num = orderOptional.get().getId();
//        }
//
//        return num;
//    }
}

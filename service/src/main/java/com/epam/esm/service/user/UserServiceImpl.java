package com.epam.esm.service.user;

import com.epam.esm.model.ListWrapper;
import com.epam.esm.model.User;
import com.epam.esm.model.filter.UserFilter;
import com.epam.esm.repository.crud.UserRepository;
import com.epam.esm.service.dto.ListWrapperDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.filter.UserFilterDto;
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
public class UserServiceImpl implements UserService<UserDto, Long, UserFilterDto> {

    private UserRepository<User, Long, UserFilter> userRepository;
    private ModelMapper mapper;

    public UserServiceImpl(UserRepository<User, Long, UserFilter> userRepository, ModelMapper mapper) {
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
        userDto.getOrders().clear();
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
    public ListWrapperDto<UserDto, UserFilterDto> getAll(UserFilterDto userFilterDto) {
        UserFilter userFilter = mapper.map(userFilterDto, UserFilter.class);
        ListWrapper<User, UserFilter> wrapper = userRepository.getAll(userFilter);
        List<UserDto> dtoList = new ArrayList<>();
        List<User> users = wrapper.getList();
        for (User c : users) {
            UserDto d = mapper.map(c, UserDto.class);
            d.getOrders().clear();
            dtoList.add(d);
        }

        ListWrapperDto<UserDto, UserFilterDto> wrapperDto = new ListWrapperDto<>();
        wrapperDto.setList(dtoList);
        UserFilter filter = wrapper.getFilter();
        wrapperDto.setFilterDto(mapper.map(filter, UserFilterDto.class));

        return wrapperDto;
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

package com.epam.esm.service.user;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.model.filter.UserFilter;
import com.epam.esm.model.wrapper.UserListWrapper;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.filter.UserFilterDto;
import com.epam.esm.dto.wrapper.UserListWrapperDto;
import com.epam.esm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private Order order;
    private Certificate certificate;
    private OrderDto orderDto;
    private ModelMapper modelMapper;
    private User user;
    private UserDto userDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        order = new Order();
        order.setDeleted(false);
        order.setTotalCost(new BigDecimal(0));
        order.setDescription("description");
        order.setCreated(Timestamp.from(Instant.now()));
        order.setId(1L);

        certificate = new Certificate();
        certificate.setDeleted(false);
        certificate.setDuration(10);
        certificate.setDescription("certificate description");
        certificate.setPrice(new BigDecimal(50));
        certificate.setName("name");
        certificate.setId(2L);
        certificate.setCreation(order.getCreated());
        order.getCertificates().add(certificate);
        modelMapper = new ModelMapper();
        orderDto = modelMapper.map(order, OrderDto.class);

        user = new User();
        user.setRole(Role.USER);
        user.setName("name");
        user.setSurname("surname");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setLogin("login");
        user.setId(1L);
        user.setDeleted(false);
        user.getOrders().add(order);

        userDto = modelMapper.map(user, UserDto.class);

    }

    @Test
    void save() {
        when(mapper.map(userDto, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(Optional.ofNullable(user));
        when(userRepository.get(1L)).thenReturn(Optional.ofNullable(user));
        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        userService.save(userDto);
        verify(userRepository).save(user);
    }

    @Test
    void get() {
        when(mapper.map(userDto, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(Optional.ofNullable(user));
        when(userRepository.get(1L)).thenReturn(Optional.ofNullable(user));
        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        userService.get(1L);
        verify(userRepository).get(1L);
    }

    @Test
    void update() {
        when(mapper.map(userDto, User.class)).thenReturn(user);
        when(userRepository.get(1L)).thenReturn(Optional.ofNullable(user));
        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        when(userRepository.update(any())).thenReturn(Optional.ofNullable(user));
        userService.update(userDto);
        verify(userRepository).update(any());
    }

    @Test
    void delete() {
        when(mapper.map(userDto, User.class)).thenReturn(user);
        when(userRepository.get(1L)).thenReturn(Optional.ofNullable(user));
        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        when(userRepository.update(any())).thenReturn(Optional.ofNullable(user));
        when(orderRepository.update(any())).thenReturn(Optional.ofNullable(order));
        userService.delete(1L);
        verify(userRepository).delete(any());
    }

    @Test
    void getAll() {
        UserFilter filter = new UserFilter();
        UserFilterDto filterDto = new UserFilterDto();
        UserListWrapper wrapper = new UserListWrapper();
        List<User> users = new ArrayList<>();
        users.add(user);
        wrapper.setFilter(filter);
        wrapper.setList(users);

        UserListWrapperDto wrapperDto = modelMapper.map(wrapper, UserListWrapperDto.class);
        when(mapper.map(user, UserDto.class)).thenReturn(userDto);
        when(mapper.map(filterDto, UserFilter.class)).thenReturn(filter);
        when(userRepository.getAll(filter)).thenReturn(wrapper);

        UserListWrapperDto all = userService.getAll(filterDto);
        assertEquals(wrapperDto, all);
    }

    @Test
    void getUserByOrderId() {
        when(userRepository.getUserByOrderId(1L)).thenReturn(Optional.ofNullable(user));
        Optional<UserDto> useDtoOptional = userService.getUserByOrderId(1L);
        verify(userRepository).getUserByOrderId(1L);
    }

    @Test
    void findByLogin() {
        when(userRepository.findByLogin(any())).thenReturn(user);
        userService.findByLogin(any());
        verify(userRepository).findByLogin(any());
    }
}
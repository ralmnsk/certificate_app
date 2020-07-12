//package com.epam.esm.service.converter;
//
//import com.epam.esm.model.Order;
//import com.epam.esm.model.Role;
//import com.epam.esm.model.User;
//import com.epam.esm.service.dto.OrderDto;
//import com.epam.esm.service.dto.UserDto;
//import org.junit.jupiter.api.Test;
//import org.modelmapper.ModelMapper;
//
//import java.math.BigDecimal;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class UserConverterTest {
//
//    @Test
//    void toEntity() {
//        ModelMapper mapper = new ModelMapper();
//        UserConverter converter = new UserConverter(mapper);
//
//        User user = new User();
//        user.setId(1L);
//        user.setLogin("login");
//        user.setName("name");
//        user.setPassword("password");
//        user.setSurname("surname");
//        user.setRole(Role.GUEST);
//        user.setDeleted(false);
//
//        Order order = new Order();
//        order.setId(1L);
//        order.setDescription("description");
//        order.setTotalCost(new BigDecimal(10.01));
//
//        user.getOrders().add(order);
//        order.setUser(user);
//
//        UserDto dto = converter.toDto(user);
//        assertEquals(user.getId(), dto.getId());
//        assertEquals(user.getLogin(), dto.getLogin());
//
//        assertEquals(dto.getOrders().size(), 1);
//        Object[] objects = dto.getOrders().toArray();
//        OrderDto orderDto = (OrderDto) objects[0];
//        assertEquals(orderDto.getDescription(), order.getDescription());
//    }
//
//    @Test
//    void toDto() {
//    }
//}
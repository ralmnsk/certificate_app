package com.epam.esm.service.deserializer;

import com.epam.esm.model.Role;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.JsonDeserializationException;
import com.epam.esm.service.user.UserService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Slf4j
//@Component
public class UserDeserializer extends StdDeserializer<UserDto> {
    private UserService<UserDto, Long> service;
    private OrderDeserializer orderDeserializer;
    private StringToRoleConverter stringToRoleConverter;

    public UserDeserializer() {
        this(null);
    }

    public UserDeserializer(Class<?> vc) {
        super(vc);
    }

    @Autowired
    public void setService(UserService<UserDto, Long> service) {
        this.service = service;
    }

    @Autowired
    public void setCertificateDeserializer(OrderDeserializer orderDeserializer) {
        this.orderDeserializer = orderDeserializer;
    }

    @Autowired
    public void setStringToRoleConverter(StringToRoleConverter stringToRoleConverter) {
        this.stringToRoleConverter = stringToRoleConverter;
    }

    @Override
    public UserDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        UserDto userDto = new UserDto();
        JsonNode node = p.getCodec().readTree(p);
        List<String> errorList = new ArrayList<>();

        try {
            Long id = node.get("id").longValue();
            if (id < 1) {
                errorList.add("id must be more then 0");
            }
            Optional<UserDto> userDtoOptional = service.get(id);
            if (userDtoOptional.isPresent()) {
                userDto = userDtoOptional.get();
                userDto.getOrders().clear();
            }
        } catch (Exception e) {
            errorList.add(e.getMessage());
        }

        try {
            String surname = node.get("surname").asText();
            if (surname == null) {
                errorList.add("surname must be not null");
            }
            if (surname.length() < 2 || surname.length() > 64) {
                errorList.add("surname must be between 2 and 64 characters");
            }
            userDto.setSurname(surname);
        } catch (Exception e) {
            errorList.add(e.getMessage());
        }

        try {
            String name = node.get("name").asText();
            if (name == null) {
                errorList.add("name must be not null");
            }
            if (name.length() < 2 || name.length() > 64) {
                errorList.add("name must be between 2 and 64 characters");
            }
            userDto.setName(name);
        } catch (Exception e) {
            errorList.add(e.getMessage());
        }

        try {
            String login = node.get("login").asText();
            if (login == null) {
                errorList.add("login must be not null");
            }
            if (login.length() < 2 || login.length() > 32) {
                errorList.add("login must be between 2 and 32 characters");
            }
            userDto.setLogin(login);
        } catch (Exception e) {
            errorList.add(e.getMessage());
        }

        try {
            String password = node.get("password").asText();
            if (password == null) {
                errorList.add("password must be not null");
            }
            if (password.length() < 2 || password.length() > 256) {
                errorList.add("login must be between 2 and 256 characters");
            }
            userDto.setPassword(password);
        } catch (Exception e) {
            errorList.add(e.getMessage());
        }

        try {
            Role role = stringToRoleConverter.convert(node.get("role").asText());
            userDto.setRole(role);
        } catch (Exception e) {
            errorList.add(e.getMessage());
        }

        if (!errorList.isEmpty()) {
            String result = errorList.stream().reduce("", (one, two) -> one + ", " + two);
            log.warn(result);
            throw new JsonDeserializationException(result);
        }

        Iterator<JsonNode> tagsIterator = node.get("orders").elements();
        Set<OrderDto> orderDtos = new HashSet<>();
        while (tagsIterator.hasNext()) {
            JsonNode orderNode = tagsIterator.next();
            OrderDto orderDto = orderDeserializer.deserializeOrderNode(orderNode);
            orderDtos.add(orderDto);
        }

        userDto.setOrders(orderDtos);

        return userDto;
    }
}

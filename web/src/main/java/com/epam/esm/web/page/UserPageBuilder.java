package com.epam.esm.web.page;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.assembler.UserAssembler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class UserPageBuilder extends AbstractPageBuilder<UserDto, UserService<UserDto, Long>, UserAssembler> {
    private final String EMPTY = "";

    public UserPageBuilder(UserService<UserDto, Long> service, UserAssembler assembler) {
        super(new HashSet<>(Arrays.asList("user.surname", "user.name")), service, assembler);
    }
}
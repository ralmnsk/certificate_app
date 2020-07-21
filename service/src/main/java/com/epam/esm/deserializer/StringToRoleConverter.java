package com.epam.esm.deserializer;

import com.epam.esm.model.Role;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter extends StdConverter<String, Role> {
    private final String USER = "user";
    private final String ADMIN = "admin";

    @Override
    public Role convert(String value) {
        Role result = Role.GUEST;
        String str = value.trim().toLowerCase();
        switch (str) {
            case USER:
                result = Role.USER;
                break;
            case ADMIN:
                result = Role.ADMIN;
                break;
        }
        return result;
    }
}

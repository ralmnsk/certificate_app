package com.epam.esm.deserializer;

import com.epam.esm.model.Role;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter extends StdConverter<String, Role> {
    private static final String USER = "user";
    private static final String ADMIN = "admin";

    @Override
    public Role convert(String value) {
        Role result;
        String str = value.trim().toLowerCase();
        switch (str) {
            case USER:
                result = Role.USER;
                break;
            case ADMIN:
                result = Role.ADMIN;
                break;
            default:
                result = Role.GUEST;
        }
        return result;
    }
}

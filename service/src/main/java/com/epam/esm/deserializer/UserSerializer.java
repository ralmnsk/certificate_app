package com.epam.esm.deserializer;

import com.epam.esm.dto.UserDto;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@JsonComponent
public class UserSerializer extends StdSerializer<UserDto> {
    public UserSerializer(Class<UserDto> t) {
        super(t);
    }

    public UserSerializer() {
        this(null);
    }

    @Override
    public void serialize(UserDto user, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", user.getId());
        gen.writeStringField("surname", user.getSurname());
        gen.writeStringField("name", user.getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticationAdmin(authentication)) {
            gen.writeStringField("email", user.getLogin());
            gen.writeStringField("role", user.getRole().toString());
        }
        gen.writeEndObject();
    }

    boolean isAuthenticationAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        List<String> authorities = authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
        return authorities.contains("ROLE_ADMIN");
    }
}

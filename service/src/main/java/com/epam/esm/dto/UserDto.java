package com.epam.esm.dto;

import com.epam.esm.deserializer.StringToRoleConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonRootName("user")
@Relation(collectionRelation = "users")
public class UserDto extends IdentifiableDto<Long> {

    @NotNull(message = "Surname must be not null")
    @Size(min = 2, max = 64, message = "User surname must be between 2 and 64 characters")
    private String surname;

    @NotNull(message = "Name must be not null")
    @Size(min = 2, max = 64, message = "User name must be between 2 and 64 characters")
    private String name;

    @JsonIgnore
    @NotNull(message = "Login must be not null")
    @Size(min = 2, max = 32, message = "Login must be between 2 and 32 characters")
    private String login;

    @JsonIgnore
    @NotNull(message = "Password must be not null")
    @Size(min = 2, max = 256, message = "Password must be between 2 and 256 characters")
    private String password;

    @JsonIgnore
    @NotNull(message = "Role must be ADMIN, USER or GUEST")
    @JsonDeserialize(converter = StringToRoleConverter.class)
    private Enum role;

    @JsonIgnore
    private boolean deleted;
    @JsonIgnore
    private Set<OrderDto> orders = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;

        UserDto userDto = (UserDto) o;

        if (deleted != userDto.deleted) return false;
        if (!surname.equals(userDto.surname)) return false;
        if (!name.equals(userDto.name)) return false;
        if (!login.equals(userDto.login)) return false;
        if (!password.equals(userDto.password)) return false;
        return role.equals(userDto.role);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + role.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }
}

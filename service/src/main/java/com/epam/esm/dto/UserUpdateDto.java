package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@JsonRootName("user")
@Relation(collectionRelation = "users")
public class UserUpdateDto extends IdentifiableDto<Long> {

    @NotNull(message = "Surname must be not null")
    @Size(min = 2, max = 64, message = "User surname must be between 2 and 64 characters")
    private String surname;

    @NotNull(message = "Name must be not null")
    @Size(min = 2, max = 64, message = "User name must be between 2 and 64 characters")
    private String name;

    @NotNull(message = "Password must be not null")
    @Size(min = 2, max = 256, message = "Password must be between 2 and 256 characters")
    private String password;

    private boolean deleted;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserUpdateDto userDto = (UserUpdateDto) o;

        if (!surname.equals(userDto.surname)) return false;
        if (!name.equals(userDto.name)) return false;
        return password.equals(userDto.password);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}

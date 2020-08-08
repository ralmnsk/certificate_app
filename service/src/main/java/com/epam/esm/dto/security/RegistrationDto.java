package com.epam.esm.dto.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
public class RegistrationDto {

    @NotNull(message = "Email must be not null")
    @Size(min = 2, max = 32, message = "Email must be between 2 and 32 characters")
    @JsonProperty("email")
//    @JsonDeserialize(converter = EmailDeserializer.class)
    @Email(message = "Incorrect email address", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String login;

    @NotNull(message = "Password must be not null")
    @Size(min = 2, max = 64, message = "Password must be between 2 and 64 characters")
    @Pattern(regexp = "([a-zA-Z0-9- .!&?#,;$]){2,64}")
    private String password;

    @NotNull(message = "Surname must be not null")
    @Size(min = 2, max = 64, message = "User surname must be between 2 and 64 characters")
    @Pattern(regexp = "([a-zA-Z0-9- .!&?#,;$]){2,64}")
    private String surname;

    @NotNull(message = "Name must be not null")
    @Size(min = 2, max = 64, message = "User name must be between 2 and 64 characters")
    @Pattern(regexp = "([a-zA-Z0-9- .!&?#,;$]){2,64}")
    private String name;

    @JsonIgnore
    private Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();


    public Collection<GrantedAuthority> getGrantedAuthoritiesList() {
        return grantedAuthoritiesList;
    }

    public void setGrantedAuthoritiesList(Collection<GrantedAuthority> grantedAuthoritiesList) {
        this.grantedAuthoritiesList = grantedAuthoritiesList;
    }

}

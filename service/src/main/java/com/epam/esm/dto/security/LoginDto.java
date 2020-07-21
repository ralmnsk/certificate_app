package com.epam.esm.dto.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class LoginDto {

    @NotNull(message = "Login must be not null")
    @Size(min = 2, max = 32, message = "Login must be between 2 and 32 characters")
    private String login;

    @NotNull(message = "Password must be not null")
    @Size(min = 2, max = 256, message = "Password must be between 2 and 256 characters")
    private String password;

}

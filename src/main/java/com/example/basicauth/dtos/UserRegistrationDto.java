package com.example.basicauth.dtos;


import com.example.basicauth.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {

    @NotNull(message = "User Name must not be null")
    @Size(min = 3, max = 10, message = "Username must be between {min} and {max} characters")
    private String username;

    @NotNull(message = "Password must not be null")
    @Size(min = 6, message = "Password at least {min} characters")
    private String password;

    @NotNull(message = "Email must not be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Role must not be null")
    private Role role;

}


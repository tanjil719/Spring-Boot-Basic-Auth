package com.example.basicauth.payloads;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {

    @NotNull(message = "User Name must not be null")
    @Size(min = 1, message = "Username must not be empty")
    private String username;

    // Exclude password Any place where the object is converted to a string
    @ToString.Exclude
    @NotNull(message = "Password must not be null")
    @Size(min = 1, message = "Password must not be empty")
    private String password;

}

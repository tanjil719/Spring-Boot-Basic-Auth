package com.example.basicauth.payloads;


import com.example.basicauth.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomPrincipal {
    private long userId;
    private String username;
    private Role role;
}

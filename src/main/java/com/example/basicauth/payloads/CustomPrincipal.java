package com.example.basicauth.payloads;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomPrincipal {
    private long userId;
    private String username;
}

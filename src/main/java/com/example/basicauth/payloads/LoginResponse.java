package com.example.basicauth.payloads;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginResponse<T> {
    private Token token;
    private T user;
}

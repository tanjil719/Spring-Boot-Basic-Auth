package com.example.basicauth.payloads;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Token {

    private String access;
    private String refresh;

}

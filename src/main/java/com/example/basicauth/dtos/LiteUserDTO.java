package com.example.basicauth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@Getter
@Setter
@ToString
public class LiteUserDTO {
    private long id;
    private String email;
}

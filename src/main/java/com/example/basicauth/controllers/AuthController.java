package com.example.basicauth.controllers;

import com.example.basicauth.dtos.LiteUserDTO;
import com.example.basicauth.models.User;
import com.example.basicauth.payloads.CustomPrincipal;
import com.example.basicauth.payloads.LoginRequest;
import com.example.basicauth.payloads.LoginResponse;
import com.example.basicauth.payloads.Token;
import com.example.basicauth.services.UserService;
import com.example.basicauth.utilities.JWTUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    @PostMapping(value = "/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid LoginRequest loginRequest) {

        try {
            //Passing the username and password to the AuthenticationManager for authentication
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            Optional<User> optionalUser = userService.findByUsername(loginRequest.getUsername());

            // Custom principal
            CustomPrincipal customPrincipal = new CustomPrincipal(
                    optionalUser.get().getId(),
                    loginRequest.getUsername(),
                    optionalUser.get().getRole()
            );

            Token token = JWTUtil.generateToken(customPrincipal);
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse<>(
                    token,
                    new LiteUserDTO(
                            optionalUser.get().getId(),
                            optionalUser.get().getEmail()            // Information of User entity use DTO to pass
                    )
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

    }

}

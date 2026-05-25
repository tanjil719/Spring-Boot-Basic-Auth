package com.example.basicauth.services;

import com.example.basicauth.dtos.LiteUserDTO;
import com.example.basicauth.dtos.UserRegistrationDto;
import com.example.basicauth.models.User;
import com.example.basicauth.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //************ Internal ****************
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //************ Secured ****************
    public User createUser(UserRegistrationDto userDto) {

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);

        return userRepository.save(user);
    }

    public Optional<LiteUserDTO> getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> new LiteUserDTO(user.getId(), user.getEmail()));
    }

}

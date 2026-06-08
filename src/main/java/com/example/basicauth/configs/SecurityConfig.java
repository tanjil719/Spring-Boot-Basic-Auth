package com.example.basicauth.configs;


import com.example.basicauth.enums.Permissions;
import com.example.basicauth.filters.JwtAuthenticationFilter;
import com.example.basicauth.services.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //For authorization spring security filter chain's Authorization filter is used
    //We need to configure in the filter chain to specify which endpoints is authorized for which role
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)                                                                               // Disable CSRF for simplicity
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()                                                       // Allow AuthController endpoints without authentication
//                                .requestMatchers("/api/users/**").hasRole("ADMIN")                                             // Only allow users with ROLE_ADMIN to access UserController endpoints
                                .requestMatchers(HttpMethod.GET,"/api/users/**").hasAuthority(Permissions.USER_READ.name())    // Only allow users with USER_READ permission to access GET endpoints of UserController
                                .requestMatchers(HttpMethod.POST,"/api/users/**").hasAuthority(Permissions.USER_CREATE.name())  // Only allow users with USER_CREATE permission to access POST endpoints of UserController
                                .requestMatchers(HttpMethod.DELETE,"/api/users/**").hasAuthority(Permissions.USER_DELETE.name())  // Only allow users with USER_DELETE permission to access DELETE endpoints of UserController
                                .anyRequest().authenticated())                                                                   // Require authentication for all other requests
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);                                         // Disable default form login and basic auth

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);   // Add our custom JWT filter before the default username/password filter

        return http.build();
    }


    // Need a custom AuthenticationManager to use our CustomUserDetailsService and password encoder
    @Bean
    public AuthenticationManager authenticationManager() {

        //Basic authentication use DaoAuthenticationProvider
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

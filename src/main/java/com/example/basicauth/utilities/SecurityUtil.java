package com.example.basicauth.utilities;

import com.example.basicauth.constants.JwtConstant;
import com.example.basicauth.enums.Role;
import com.example.basicauth.payloads.CustomPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SecurityUtil {

    public static Optional<String> getTokenFromRequestHeader(HttpServletRequest request) {

        String header = request.getHeader(JwtConstant.HEADER_STRING);
        if (header != null && header.startsWith(JwtConstant.TOKEN_PREFIX)) {
            return Optional.of(header.replace(JwtConstant.TOKEN_PREFIX, ""));
        }
        return Optional.empty();
    }

    /**
     * <T> is a generic type parameter placeholder.
     * It makes the method generic — meaning it can accept and return ANY type of data.
     * But in here it is not used.
     */
//    public static <T> UsernamePasswordAuthenticationToken getAuthentication(CustomPrincipal customPrincipal) {
//        return new UsernamePasswordAuthenticationToken(
//                customPrincipal,
//                null,
//                new ArrayList<>()
//        );
//
//    }

    /**
     * Here it is implemented with generic return type and parameter. That made the method generic.
     * Generic Types:
     * <T> - the type of principal object (input parameter)
     * <R> - the type of authentication token to return (output)
     * This makes the method flexible to work with:
     * Any type of principal (CustomPrincipal, String, User, etc.)
     * Any type of authentication token (UsernamePasswordAuthenticationToken, OAuth2Token, etc.)
     */
    public static <T, R> R getAuthentication(T principal, Class<R> tokenType) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (principal instanceof CustomPrincipal) {
            CustomPrincipal cp = (CustomPrincipal) principal;
            Role role = cp.getRole();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

            authorities.addAll(role.getPermissions().stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.name()))
                    .toList());
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal,              // Accepts any type T
                null,
                authorities
        );
        return tokenType.cast(token);  // Returns any type R
    }

}


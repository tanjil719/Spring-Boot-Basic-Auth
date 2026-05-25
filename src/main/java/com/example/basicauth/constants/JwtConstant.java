package com.example.basicauth.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtConstant {

    public static String SECRET;
    public static String HEADER_STRING;
    public static String TOKEN_PREFIX;
    public static long EXPIRATION_TIME;
    public static long REFRESH_TOKEN_EXPIRATION_TIME;



    @Value("${token.secret}")
    public void setSecret(String secret) {
        SECRET = secret;
    }

    @Value("${token.header-string}")
    public void setHeaderString(String headerString) {
        HEADER_STRING = headerString;
    }

    @Value("${token.prefix}")
    public void setTokenPrefix(String tokenPrefix) {
        TOKEN_PREFIX = tokenPrefix + " ";
    }

    @Value("${token.expiration-time}")
    public void setExpirationTime(long expirationTime) {
        EXPIRATION_TIME = expirationTime;
    }

    @Value("${token.expiration-time}")
    public void setRefreshTokenExpirationTime(long expirationTime) {
        REFRESH_TOKEN_EXPIRATION_TIME = expirationTime + 300000;
    }

}

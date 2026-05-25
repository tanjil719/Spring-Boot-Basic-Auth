package com.example.basicauth.utilities;

import com.example.basicauth.constants.JwtConstant;
import com.example.basicauth.payloads.CustomPrincipal;
import com.example.basicauth.payloads.Token;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

/**
 * DESIGN: Stateless (doesn't store or remember any information between method calls) utility class with static methods.
 * - NOT marked as @Component because it has no state, Spring dependencies (Doesn't need other Spring beans injected),
 * Spring not need to manage its lifecycle
 * - All methods are static
 * - Direct usage: JWTUtil.generateToken(...)
 */

public class JWTUtil {


    public static Token generateToken(CustomPrincipal customPrincipal) {

        Date issuedAt = new Date();
        Date expiryDate = calculateExpirationDate(JwtConstant.EXPIRATION_TIME);
        Date refreshExpiryDate = calculateExpirationDate(JwtConstant.REFRESH_TOKEN_EXPIRATION_TIME);

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtConstant.SECRET));

        return new Token(
                Jwts.builder()
                        .claim("CURRENT_USER", customPrincipal)
                        .setSubject(customPrincipal.getUsername())
                        .setIssuedAt(issuedAt)
                        .setExpiration(expiryDate)
                        .compressWith(CompressionCodecs.DEFLATE)     //To compress the token's payload and reduce its size
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact(),
                Jwts.builder()
                        .setSubject(customPrincipal.getUsername())
                        .setIssuedAt(issuedAt)
                        .setExpiration(refreshExpiryDate)
                        .compressWith(CompressionCodecs.DEFLATE)
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact()
        );
    }

    private static Date calculateExpirationDate(long expirationTime) {
        return new Date(System.currentTimeMillis() + expirationTime);
    }

}

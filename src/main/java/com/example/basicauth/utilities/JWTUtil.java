package com.example.basicauth.utilities;

import com.example.basicauth.constants.JwtConstant;
import com.example.basicauth.payloads.CustomPrincipal;
import com.example.basicauth.payloads.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;

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

        //key is a cryptographic signing key used to digitally sign and verify JWT tokens
        //The secret key is typically a random string and The signing algorithm (HS256) requires bytes, not text
        //Decoders.BASE64.decode() convert it to bytes and Keys.hmacShaKeyFor() creates a Key object from the byte array
        //It tells signing algorithm What type of key this is (HMAC key for HS256)
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtConstant.SECRET));

        return new Token(
                Jwts.builder()
                        .claim("CURRENT_USER", customPrincipal)            // part of payload
                        .setSubject(customPrincipal.getUsername())               // part of payload
                        .setIssuedAt(issuedAt)                                   // part of payload
                        .setExpiration(expiryDate)                               // part of payload
                        .compressWith(CompressionCodecs.DEFLATE)     //To compress the token's payload and reduce its size
                        .signWith(key, SignatureAlgorithm.HS256)     //uses: header + payload + secret_key
                        .compact(),                                  //produces: header.payload.signature
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

    public static boolean isValidToken(String token) {
        return !StringUtils.isEmpty(token) && !isTokenExpired(token);
    }

    private static boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }


    private static Date getExpirationDate(String token) {
        return getAllClaims(token).getExpiration();
    }


    // Claim is a piece of custom information stored inside the JWT token's payload.
    private static Claims getAllClaims(String token) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtConstant.SECRET));

        return Jwts.parserBuilder()
                .setSigningKey(key)               //Set the key for verification
                .build()
                .parseClaimsJws(token)            //Parse and verify the token
                .getBody();                       //Extract the claims (payload)
    }

    public static CustomPrincipal getTokenHolderDetails(String token) {
        return convertObject(getAllClaims(token).get("CURRENT_USER"), CustomPrincipal.class);
    }

    public static <T> T convertObject(Object fromValue, Class<T> toValueType) {
        return new ObjectMapper().convertValue(fromValue, toValueType);
    }

}

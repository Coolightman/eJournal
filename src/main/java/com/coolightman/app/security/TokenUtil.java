package com.coolightman.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.coolightman.app.security.SecurityConstants.*;

/**
 * The type Token util.
 */
@Component
public class TokenUtil {

    @Value("@{jwt.secret}")
    private String SECRET;

    /**
     * Gets username from token.
     *
     * @param token the token
     * @return the username from token
     */
    //retrieve username from jwt token
    String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    /**
     * Generate token string.
     *
     * @param username the username
     * @return the string
     */
    //generate token for user
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return BEARER + Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_MSEC))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }
}

package com.coolightman.app.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

import static com.coolightman.app.security.SecurityConstants.BEARER;
import static com.coolightman.app.security.SecurityConstants.TOKEN_VALIDITY_MILLIS;

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
    String getUsernameFromToken(final String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Generate token string.
     *
     * @param username the username
     * @return the string
     */
    public String generateToken(final String username) {

        return BEARER + Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_MILLIS))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }
}

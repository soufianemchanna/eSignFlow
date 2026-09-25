package com.esignflow.document_service.auth.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtService {
    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(
            @Value("${esignflow.jwt.secret}") String secret,
            @Value("${esignflow.jwt.expiration}") long expirationMs
    ){
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    public String generateToken(String subjectEmail) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(subjectEmail)
                .claim("email", "user")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }
    
    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser().verifyWith(signingKey).build()
                .parseSignedClaims(token).getPayload();
        return resolver.apply(claims);
    }

    public boolean isValid(String token, String expectedEmail){
        return extractEmail(token).equals(expectedEmail) && !isExpired(token);
    }

    public boolean isExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public long getExpirationMs(){return expirationMs;}


}

package com.microservices.stylecartbackend.auth.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Generate Access Token
     */
    public String generateAccessToken(UserDetails userDetails) {

        return buildToken(
                new HashMap<>(),
                userDetails,
                accessTokenExpiration
        );
    }

    /**
     * Generate Refresh Token
     */
    public String generateRefreshToken(UserDetails userDetails) {

        return buildToken(
                new HashMap<>(),
                userDetails,
                refreshTokenExpiration
        );
    }

    /**
     * Common JWT Builder
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extract Username (Subject)
     */
    public String extractUsername(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }

    /**
     * Extract Expiration
     */
    public Date extractExpiration(String token) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }

    /**
     * Extract Any Claim
     */
    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    /**
     * Parse JWT
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Check Expiration
     */
    public boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }

    /**
     * Validate JWT
     */
    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

}
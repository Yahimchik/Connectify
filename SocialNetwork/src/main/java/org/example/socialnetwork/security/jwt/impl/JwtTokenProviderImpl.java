package org.example.socialnetwork.security.jwt.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.socialnetwork.entities.User;
import org.example.socialnetwork.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Component
@Slf4j
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;
    private final UserDetailsService userDetailsService;

    public JwtTokenProviderImpl(
            @Value("${spring.application.security.jwt.secret.access}") String jwtAccessSecret,
            @Value("${spring.application.security.jwt.secret.refresh}") String jwtRefreshSecret,
            @Qualifier("userDetailServiceImpl") UserDetailsService userDetailsService) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String generateAccessToken(@NonNull User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("role", user.getRole());
        claims.put("status", user.getStatus());

        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now
                .plusMinutes(15)
                .atZone(ZoneId
                        .systemDefault())
                .toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .compact();
    }

    @Override
    public String generateRefreshToken(@NonNull User user) {
        LocalDateTime now = LocalDateTime.now();
        Instant refreshExpirationInstant = now
                .plusDays(30)
                .atZone(ZoneId
                        .systemDefault())
                .toInstant();
        Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    @Override
    public boolean validateAccessToken(@NonNull String token) {
        return validateToken(token, jwtAccessSecret);
    }

    @Override
    public boolean validateRefreshToken(@NonNull String token) {
        return validateToken(token, jwtRefreshSecret);
    }

    @Override
    public Claims getAccessClaims(@NonNull String token) {
        return getClaimsFromToken(token, jwtAccessSecret);
    }

    @Override
    public Claims getRefreshClaims(@NonNull String token) {
        return getClaimsFromToken(token, jwtRefreshSecret);
    }

    @Override
    public Authentication getAuthentication(@NonNull String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getLoginFromAccessToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Override
    public String getLoginFromAccessToken(@NonNull String token) {
        Claims claims = getAccessClaims(token);
        return claims.getSubject();
    }

    @Override
    public String getLoginFromRefreshToken(@NonNull String token) {
        Claims claims = getRefreshClaims(token);
        return claims.getSubject();
    }

    @Override
    public LocalDateTime getExpirationDate(@NonNull String token) {
        Claims claims = getRefreshClaims(token);
        Date expiration = claims.getExpiration();
        return expiration
                .toInstant()
                .atZone(ZoneId
                        .systemDefault())
                .toLocalDateTime();
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    private Claims getClaimsFromToken(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

package com.github.alekseypetkun.TransportTicketApp.security.jwt;

import com.github.alekseypetkun.TransportTicketApp.security.custom.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Бизнес-логика по генерации токенов
 */
@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret.access}")
    private String jwtAccessSecret; // Секретный ключ для токена-доступа
    @Value("${jwt.secret.refresh}")
    private String jwtRefreshSecret; // Секретный ключ для токена-обновления
    @Value("${jwt.secret.expiration_access_token}")
    private Integer expirationInMinutes; // Время жизни access-токена в минутах
    @Value("${jwt.secret.expiration_refresh_token}")
    private Integer expirationInDays; // Время жизни refresh-токена в днях
    @Value("${jwt.secret.issuer}")
    private String issuer; // Кто выдал токен

    public String generateAccessToken(@NonNull CustomUserDetails user) {

        Map<String, Object> claims = new HashMap<>() {{
            put("role", user.getUserByRole());
            put("username", user.getUsername());
        }};
        return generateAccessToken(claims, String.valueOf(user.getUserById()));
    }

    public String generateAccessToken(Map<String, Object> claims, String subject) {

        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(expirationInMinutes)
                .atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);

        return generateAccessToken(accessExpiration, claims, subject);
    }

    public String generateAccessToken(Date expirationDate, Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setExpiration(expirationDate)
                .setId(UUID.randomUUID().toString()) // рандомный id токена
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret))) // Подпись токена
                .compact();
    }

    public String generateRefreshToken(@NonNull CustomUserDetails user) {

        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(expirationInDays)
                .atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserById()))
                .setExpiration(refreshExpiration)
                .setId(UUID.randomUUID().toString()) // рандомный id токена
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret)))
                .compact();
    }

    public boolean validateAccessToken(@NonNull String accessToken) {

        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {

        return validateToken(refreshToken, jwtRefreshSecret);
    }

    private boolean validateToken(@NonNull String token, String secret) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Срок токена истёк", expEx);

        } catch (UnsupportedJwtException unsEx) {
            log.error("Неподдерживаемый jwt", unsEx);

        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);

        } catch (SignatureException sEx) {
            log.error("Невалидная сигнатура", sEx);

        } catch (Exception e) {
            log.error("Невалидный токен", e);
        }
        return false;
    }

    public Claims getAccessClaims(@NonNull String token) {

        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(@NonNull String token) {

        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(@NonNull String token, String secret) {

        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
package com.github.wesleybritovlk.fullchatj.infra;

import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import javax.crypto.SecretKey;

import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthPayload;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthResponse;
import com.google.inject.Inject;

import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.val;

public interface JwtProvider {
    AuthResponse createToken(AuthPayload.User payload);

    boolean isExpired(String token);

    AuthResponse getAuthResponse(String authorization);

    JwtPayload getPayload(String token);

    JwtPayload getPayloadValid(String authorization);

    @Builder
    record JwtPayload(
            String subject,
            Date expiration) {
    }
}

@RequiredArgsConstructor(onConstructor = @__(@Inject))
class JwtProviderImpl implements JwtProvider {
    private final Jwt jwt;
    private final Properties properties;

    private static final String TOKEN_PREFIX = "Bearer ";

    public AuthResponse createToken(AuthPayload.User request) {
        val claims = Map.of("scope", (Object) request.scopes());
        val token = jwt.buildToken(claims, request.email());
        val expiration = Long.parseLong(properties.getProperty("jwt.expiration"));
        return AuthResponse.builder()
                .accessToken(token)
                .expiresIn(expiration)
                .build();
    }

    @Override
    public boolean isExpired(String token) {
        if (token == null)
            return true;
        try {
            return jwt.getClaim(token, Claims::getExpiration).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public AuthResponse getAuthResponse(String authorization) {
        val token = authorization != null ? authorization.substring(TOKEN_PREFIX.length()) : null;
        if (!isExpired(token)) {
            val expiration = jwt.getClaim(token, Claims::getExpiration).toInstant();
            return AuthResponse.builder()
                    .accessToken(token)
                    .expiresIn(expiration.toEpochMilli())
                    .build();
        }
        return AuthResponse.builder().build();
    }

    @Override
    public JwtPayload getPayload(String token) {
        val subject = jwt.getClaim(token, Claims::getSubject);
        val expiration = jwt.getClaim(token, Claims::getExpiration);
        return JwtPayload.builder()
                .subject(subject)
                .expiration(expiration)
                .build();
    }

    @Override
    public JwtPayload getPayloadValid(String authorization) {
        val token = this.getAuthResponse(authorization).accessToken();
        if (token == null || token.isBlank() || isExpired(token))
            throw new UnauthorizedResponse();
        return this.getPayload(token);
    }

}

@RequiredArgsConstructor(onConstructor = @__(@Inject))
class Jwt {
    private final Properties properties;

    private SecretKey secretKey() {
        val secret = properties.getProperty("jwt.secret");
        val base64 = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(base64);
    }

    private Claims extractClaims(String token) {
        val jwtParser = Jwts.parser().verifyWith(secretKey()).build();
        val signedClaims = jwtParser.parseSignedClaims(token);
        return signedClaims.getPayload();
    }

    protected <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        val claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    protected String buildToken(Map<String, Object> claims, String subject) {
        val expiration = Long.parseLong(properties.getProperty("jwt.expiration"));
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(secretKey())
                .compact();
    }
}

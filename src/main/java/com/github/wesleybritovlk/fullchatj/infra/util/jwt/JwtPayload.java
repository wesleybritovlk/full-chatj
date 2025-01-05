package com.github.wesleybritovlk.fullchatj.infra.util.jwt;

import java.util.Date;

import lombok.Builder;

@Builder
public record JwtPayload(
        String subject,
        Date expiration) {
}

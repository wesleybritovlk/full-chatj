package com.github.wesleybritovlk.fullchatj.app.auth.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequest {
    public record Login(
            String email,
            String password,
            String platform) {
    }

    public record Register(
            String email,
            String password,
            String fullName,
            String birthDate,
            String platform) {
    }
}

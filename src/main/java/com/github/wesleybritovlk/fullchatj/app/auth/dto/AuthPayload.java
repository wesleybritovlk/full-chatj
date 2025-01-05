package com.github.wesleybritovlk.fullchatj.app.auth.dto;

import java.util.List;

import com.github.wesleybritovlk.fullchatj.app.auth.AuthEnum;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthPayload {
    @Builder
    public record User(String email, List<AuthEnum.Scope> scopes) {
    }
}

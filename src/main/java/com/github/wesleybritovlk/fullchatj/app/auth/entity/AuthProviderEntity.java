package com.github.wesleybritovlk.fullchatj.app.auth.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.github.wesleybritovlk.fullchatj.app.auth.AuthEnum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthProviderEntity {
    private UUID id;

    private AuthEnum.Provider name;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}

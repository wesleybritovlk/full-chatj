package com.github.wesleybritovlk.fullchatj.app.auth.entity;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.github.wesleybritovlk.fullchatj.app.auth.AuthEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "tb_auth_user", schema = "auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "full_name")
    private String fullName;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    private String email;
    private String password;
    @Column(name = "avatar_url")
    private String avatarUrl;

    private List<UUID> scopes;
    private List<UUID> providers;
    @Enumerated(value = EnumType.STRING)
    private List<AuthEnum.Platform> platforms;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;
}

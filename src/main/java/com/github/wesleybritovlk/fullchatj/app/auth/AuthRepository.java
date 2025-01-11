package com.github.wesleybritovlk.fullchatj.app.auth;

import com.github.wesleybritovlk.fullchatj.infra.util.entitymanager.EntityManagerProvider;
import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AuthRepository {
    private final EntityManagerProvider entityManagerProvider;

}

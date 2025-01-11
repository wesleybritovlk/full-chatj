package com.github.wesleybritovlk.fullchatj.app.auth;

import com.github.wesleybritovlk.fullchatj.app.auth.entity.AuthUserEntity;
import com.github.wesleybritovlk.fullchatj.infra.util.EntityManagerProvider;
import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AuthRepository {
    private final EntityManagerProvider entityManagerProvider;

    public void save(AuthUserEntity entity) {
        entityManagerProvider.execute(em -> em.persist(entity));
    }

}

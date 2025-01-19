package com.github.wesleybritovlk.fullchatj.app.auth;

import java.util.Optional;

import com.github.wesleybritovlk.fullchatj.app.auth.entity.AuthUserEntity;
import com.google.inject.Inject;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

public interface AuthRepository {
    AuthUserEntity save(AuthUserEntity entity);

    Optional<AuthUserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class AuthRepositoryImpl implements AuthRepository {
    private final EntityManager em;

    @Override
    public AuthUserEntity save(AuthUserEntity entity) {
        val transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(entity);
            em.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive())
                transaction.rollback();
            throw e;
        }
        return entity;
    }

    @Override
    public Optional<AuthUserEntity> findByEmail(String email) {
        val query = em.createNativeQuery("""
                SELECT *
                FROM auth.tb_auth_user
                WHERE email = ?
                AND is_deleted = FALSE
                """, AuthUserEntity.class);
        query.setParameter(1, email);
        try {
            return Optional.ofNullable((AuthUserEntity) query.getSingleResult());
        } catch (Exception e) {
            log.error("Error while finding user by email: {}", email, e.getMessage());
            return Optional.empty();
        }
    }

    public boolean existsByEmail(String email) {
        val query = em.createNativeQuery("""
                SELECT EXISTS(
                    SELECT 1
                    FROM auth.tb_auth_user
                    WHERE email = ?
                    AND is_deleted = FALSE
                )
                """);
        query.setParameter(1, email);
        return (boolean) query.getSingleResult();
    }

}

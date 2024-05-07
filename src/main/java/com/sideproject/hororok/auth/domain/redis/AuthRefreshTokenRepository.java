package com.sideproject.hororok.auth.domain.redis;

import com.sideproject.hororok.auth.exception.NoSuchTokenException;
import org.springframework.data.repository.CrudRepository;

public interface AuthRefreshTokenRepository extends CrudRepository<AuthRefreshToken, Long> {



    default AuthRefreshToken getById(final Long memberId) {
        return findById(memberId)
                .orElseThrow(NoSuchTokenException::new);
    }

}

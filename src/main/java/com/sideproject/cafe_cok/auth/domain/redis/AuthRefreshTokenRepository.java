package com.sideproject.cafe_cok.auth.domain.redis;

import com.sideproject.cafe_cok.auth.exception.NoSuchRefreshTokenException;
import org.springframework.data.repository.CrudRepository;

public interface AuthRefreshTokenRepository extends CrudRepository<AuthRefreshToken, Long> {

    default AuthRefreshToken getById(final Long memberId) {
        return findById(memberId)
                .orElseThrow(() ->
                        new NoSuchRefreshTokenException("[ID : " + memberId + "] 에 해당하는 Refresh Token 이 존재하지 않습니다."));
    }

}

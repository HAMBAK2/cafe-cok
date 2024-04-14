package com.sideproject.hororok.auth.domain.redis;

import org.springframework.data.repository.CrudRepository;

public interface AuthRefreshTokenRepository extends CrudRepository<AuthRefreshToken, Long> {

}

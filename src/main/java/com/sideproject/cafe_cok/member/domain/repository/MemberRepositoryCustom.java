package com.sideproject.cafe_cok.member.domain.repository;

import java.time.LocalDateTime;

public interface MemberRepositoryCustom {

    void update(final Long memberId,
                final String nickname,
                final String picture);

    void update(final Long memberId,
                final LocalDateTime deletedAt);
}

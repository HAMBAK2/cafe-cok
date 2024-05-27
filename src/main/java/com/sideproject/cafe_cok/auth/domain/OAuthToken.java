package com.sideproject.cafe_cok.auth.domain;

import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Table(name = "oauth_tokens")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id", nullable = false)
    private Member member;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;


    public OAuthToken(final Member member, final String refreshToken) {
        this.member = member;
        this.refreshToken = refreshToken;
    }

    public void changeRefreshToken(final String refreshToken) {
        if(!Objects.isNull(refreshToken)) {
            this.refreshToken = refreshToken;
        }
    }
}

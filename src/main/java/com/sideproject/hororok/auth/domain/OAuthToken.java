package com.sideproject.hororok.auth.domain;

import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Table(name = "oauth_tokens")
@Getter
@Entity
public class OAuthToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id", nullable = false)
    private Member member;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    protected OAuthToken(){}

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

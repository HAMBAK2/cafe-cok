package com.sideproject.cafe_cok.member.domain;


import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.member.domain.enums.SocialType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "withdrawn_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawnMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "picture")
    private String picture;

    @Column(name = "nickname")
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    @Column(name = "withdrawn_date", nullable = false)
    private LocalDateTime withdrawnDate;

    public WithdrawnMember(final Member member) {
        this.email = member.getEmail();
        this.picture = member.getPicture();
        this.nickname = member.getNickname();
        this.socialType = member.getSocialType();
        this.withdrawnDate = LocalDateTime.now();
    }

}

package com.sideproject.hororok.member.domain;


import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.member.domain.enums.SocialType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "withdrawn_members")
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

    protected WithdrawnMember() {

    }

    public WithdrawnMember(final Member member) {
        this.email = member.getEmail();
        this.picture = member.getPicture();
        this.nickname = member.getNickname();
        this.socialType = member.getSocialType();
        this.withdrawnDate = LocalDateTime.now();
    }

}

package com.sideproject.hororok.member.domain;

import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.member.exception.InvalidMemberException;
import com.sideproject.hororok.review.domain.Review;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Table(name = "members")
@Entity
public class Member extends BaseEntity {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

//    @OneToMany(mappedBy = "member")
//    private List<Review> reviews = new ArrayList<>();

    protected Member() {

    }

    public Member(final String email, final String nickname, final SocialType socialType) {
        validateEmail(email);

        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
    }

    private void validateEmail(final String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if(!matcher.matches()) {
            throw new InvalidMemberException("이메일 형식이 올바르지 않습니다.");
        }
    }


}

package com.sideproject.cafe_cok.member.domain;

import com.sideproject.cafe_cok.bookmark.domain.Bookmark;
import com.sideproject.cafe_cok.bookmark.domain.BookmarkFolder;
import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.member.domain.enums.SocialType;
import com.sideproject.cafe_cok.member.exception.InvalidMemberException;
import com.sideproject.cafe_cok.utils.Constants;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "member")
    private List<BookmarkFolder> bookmarkFolders = new ArrayList<>();

    public Member(final String email,
                  final String nickname,
                  final SocialType socialType) {

        validateEmail(email);
        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
    }

    private void validateEmail(final String email) {

        Matcher matcher = Constants.EMAIL_PATTERN.matcher(email);
        if(!matcher.matches()) {
            throw new InvalidMemberException("이메일 형식이 올바르지 않습니다.");
        }
    }

    public void changeNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void changePicture(final String picture) {
        this.picture = picture;
    }


}

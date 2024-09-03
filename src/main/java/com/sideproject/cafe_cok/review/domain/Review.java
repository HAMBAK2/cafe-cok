package com.sideproject.cafe_cok.review.domain;

import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.keword.domain.CafeReviewKeyword;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.review.dto.request.ReviewSaveRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "specialNote", length = 1000)
    private String specialNote;

    @Column(name = "star_rating")
    private Integer starRating;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafes_id")
    private Cafe cafe;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "members_id")
    private Member member;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<CafeReviewKeyword> cafeReviewKeywords = new ArrayList<>();

    public Review(final String content,
                  final String specialNote,
                  final Integer starRating,
                  final Cafe cafe,
                  final Member member) {
        this.content = content;
        this.specialNote = specialNote;
        this.starRating = starRating;
        this.cafe = cafe;
        if(member != null) changeMember(member);
    }

    public Review(final ReviewSaveRequest request,
                  final Cafe cafe,
                  final Member member) {
        this(request.getContent(), request.getSpecialNote(), request.getStarRating(), cafe, member);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public void setStarRating(Integer starRating) {
        this.starRating = starRating;
    }

    public void changeMember(final Member member) {
        this.member = member;
        member.getReviews().add(this);
    }

}

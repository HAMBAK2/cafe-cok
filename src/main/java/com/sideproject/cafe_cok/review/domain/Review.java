package com.sideproject.cafe_cok.review.domain;

import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "content")
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


    protected Review() {
    }

    public Review(final String content, final String specialNote, final Integer starRating,
                  final Cafe cafe, final Member member) {
        this.content = content;
        this.specialNote = specialNote;
        this.starRating = starRating;
        this.cafe = cafe;
        this.member = member;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public void changeStarRating(Integer starRating) {
        this.starRating = starRating;
    }

}
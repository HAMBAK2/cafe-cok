package com.sideproject.hororok.review.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafes_id")
    private Cafe cafe;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "members_id")
    private Member member;

    @OneToMany(mappedBy = "review")
    private List<ReviewImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "review")
    private List<CafeReviewKeyword> cafeReviewKeywords = new ArrayList<>();



}

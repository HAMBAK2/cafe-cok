package com.sideproject.hororok.review.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.entity.BaseEntity;
import com.sideproject.hororok.keword.entity.Keyword;
import com.sideproject.hororok.reviewImage.entity.ReviewImage;
import com.sideproject.hororok.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    @Column(length = 1000)
    private String specialNote;

    private Integer starRating;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CAFE_ID")
    private Cafe cafe;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "REVIEW_ID")
    private List<ReviewImage> images = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "REVIEW_KEYWORD",
            joinColumns = @JoinColumn(name = "REVIEW_ID"),
            inverseJoinColumns = @JoinColumn(name = "KEYWORD_ID")
    )
    private List<Keyword> keywords = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void addKeyword(Keyword keyword) {
        this.keywords.add(keyword);
        keyword.getReviews().add(this);
    }
}

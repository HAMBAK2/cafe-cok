package com.sideproject.hororok.review.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.keword.entity.Keyword;
import com.sideproject.hororok.reviewImage.entity.ReviewImage;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Lob
    private String content;

    @Column(length = 1000)
    private String specialNote;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CAFE_ID")
    private Cafe cafe;

    @JsonIgnore
    @OneToMany(mappedBy = "review")
    private List<ReviewImage> images = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "REVIEW_KEYWORD",
            joinColumns = @JoinColumn(name = "REVIEW_ID"),
            inverseJoinColumns = @JoinColumn(name = "KEYWORD_ID")
    )
    private List<Keyword> keywords = new ArrayList<>();
}

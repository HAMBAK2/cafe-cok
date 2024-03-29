package com.sideproject.hororok.reviewImage.entity;

import com.sideproject.hororok.review.Entity.Review;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "REVIEW_ID")
    private Review review;
}

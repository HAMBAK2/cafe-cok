package com.sideproject.hororok.reviewImage.entity;

import com.sideproject.hororok.entity.BaseEntity;
import com.sideproject.hororok.review.Entity.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@NoArgsConstructor
public class ReviewImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String imageUrl;



    public ReviewImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}

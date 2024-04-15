package com.sideproject.hororok.review.domain;

import com.sideproject.hororok.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "review_images")
public class ReviewImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Lob
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviews_id")
    private Review review;


    public ReviewImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}

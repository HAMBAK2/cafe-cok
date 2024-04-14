package com.sideproject.hororok.reviewImage.entity;

import com.sideproject.hororok.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@NoArgsConstructor
public class ReviewImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Lob
    private String imageUrl;



    public ReviewImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}

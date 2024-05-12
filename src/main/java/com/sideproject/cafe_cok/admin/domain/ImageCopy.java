package com.sideproject.cafe_cok.admin.domain;


import com.sideproject.cafe_cok.admin.domain.CafeCopy;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.review.domain.Review;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table(name = "images_copy")
public class ImageCopy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "image_type", nullable = false)
    private ImageType imageType;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafes_copy_id")
    private CafeCopy cafeCopy;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviews_id")
    private Review review;

    protected ImageCopy() {
    }

    public ImageCopy(final ImageType imageType, final String imageUrl, final Review review) {
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.review = review;
    }

    public ImageCopy(final ImageType imageType, final String imageUrl, final CafeCopy cafeCopy) {
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.cafeCopy = cafeCopy;
    }
}

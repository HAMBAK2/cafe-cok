package com.sideproject.cafe_cok.image.domain;


import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.review.domain.Review;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table(name = "images")
public class Image extends BaseEntity {

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
    @JoinColumn(name = "cafes_id")
    private Cafe cafe;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviews_id")
    private Review review;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menus_id")
    private Menu menu;

    protected Image() {
    }

    public Image(final ImageType imageType, final String imageUrl, final Cafe cafe) {
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.cafe = cafe;
    }

    public Image(final ImageType imageType, final String imageUrl, final Cafe cafe, final Review review) {
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.cafe = cafe;
        this.review = review;
    }

    public Image(final ImageType imageType, final String imageUrl, final Cafe cafe, final Menu menu) {
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.cafe = cafe;
        this.menu = menu;
    }


}

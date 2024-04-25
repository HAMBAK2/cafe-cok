package com.sideproject.hororok.cafe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@Table(name = "cafe_images")
public class CafeImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafes_id")
    private Cafe cafe;

    protected CafeImage() {
    }

    public CafeImage(final String imageUrl, final Cafe cafe) {
        this.imageUrl = imageUrl;
        this.cafe = cafe;
    }
}

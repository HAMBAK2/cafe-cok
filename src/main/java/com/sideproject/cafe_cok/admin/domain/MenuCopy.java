package com.sideproject.cafe_cok.admin.domain;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@Table(name = "menus_copy")
public class MenuCopy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Lob
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafes_copy_id")
    private CafeCopy cafeCopy;

    protected MenuCopy() {
    }

    public MenuCopy(final String name, final Integer price, final String imageUrl, final CafeCopy cafeCopy) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.cafeCopy = cafeCopy;
    }
}

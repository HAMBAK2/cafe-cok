package com.sideproject.cafe_cok.menu.domain;

import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.cafe.domain.Cafe;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@Table(name = "menus")
public class Menu extends BaseEntity {

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
    @JoinColumn(name = "cafes_id")
    private Cafe cafe;

    protected Menu() {
    }

    public Menu(final String name, final Integer price, final String imageUrl, final Cafe cafe) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.cafe = cafe;
    }
}

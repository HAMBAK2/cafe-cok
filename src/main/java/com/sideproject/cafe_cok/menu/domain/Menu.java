package com.sideproject.cafe_cok.menu.domain;

import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.cafe.domain.Cafe;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@Table(name = "menus")
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafes_id")
    private Cafe cafe;

    @Builder
    public Menu(final Long id,
                final String name,
                final Integer price,
                final Cafe cafe) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cafe = cafe;
    }

    public void changeName(final String name) {
        this.name = name;
    }

    public void changePrice(final Integer price) {
        this.price = price;
    }
}

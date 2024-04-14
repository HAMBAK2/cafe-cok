package com.sideproject.hororok.menu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.cafe.entity.Cafe;

import com.sideproject.hororok.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private Integer price;

    @Lob
    private String imageUrl;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CAFE_ID")
    private Cafe cafe;
}

package com.sideproject.hororok.cafeImage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.cafe.entity.Cafe;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
public class CafeImage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String imageUrl;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CAFE_ID")
    private Cafe cafe;

}

package com.sideproject.hororok.cafe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.Menu.entity.Menu;
import com.sideproject.hororok.image.entity.Image;
import com.sideproject.hororok.review.Entity.Review;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
public class Cafe {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String phoneNumber;

    private String roadAddress;

    private BigDecimal longitude; //경도

    private BigDecimal latitude; //위도

    private String BusinessHours;

    private String closedDay;

    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;

    @Column(precision = 2, scale = 1)
    private BigDecimal starRating;

    @JsonIgnore
    @OneToMany(mappedBy = "cafe")
    private List<Menu> menus = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "cafe")
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "cafe")
    private List<Image> images = new ArrayList<>();
}

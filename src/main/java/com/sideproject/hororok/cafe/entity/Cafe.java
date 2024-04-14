package com.sideproject.hororok.cafe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.menu.entity.Menu;
import com.sideproject.hororok.cafeImage.entity.CafeImage;
import com.sideproject.hororok.operationHours.entity.OperationHour;
import com.sideproject.hororok.review.Entity.Review;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;


@Getter
@Entity
public class Cafe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String phoneNumber;

    private String roadAddress;

    @Column(precision = 17, scale = 14)
    private BigDecimal longitude; //경도

    @Column(precision = 17, scale = 14)
    private BigDecimal latitude; //위도


    @Column(precision = 2, scale = 1)
    private BigDecimal starRating;

    private Long reviewCount;

    @JsonIgnore
    @OneToMany(mappedBy = "cafe")
    private List<Menu> menus = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "cafe")
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "cafe")
    private List<CafeImage> images = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "cafe")
    private List<OperationHour> operationHours = new ArrayList<>();
}

package com.sideproject.hororok.keword.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.category.entity.Category;
import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.review.Entity.Review;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Getter
@Entity
public class Keyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @JsonIgnore
    @ManyToMany(mappedBy = "keywords")
    private List<Review> reviews = new ArrayList<>();
}

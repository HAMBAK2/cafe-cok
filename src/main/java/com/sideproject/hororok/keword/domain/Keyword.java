package com.sideproject.hororok.keword.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.category.domain.Category;
import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.review.domain.Review;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Getter
@Entity
@Table(name = "keywords")
public class Keyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @JsonIgnore
    @ManyToMany(mappedBy = "keywords")
    private List<Review> reviews = new ArrayList<>();
}

package com.sideproject.hororok.keword.entity;

import com.sideproject.hororok.category.entity.Category;
import com.sideproject.hororok.review.Entity.Review;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
public class Keyword {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @ManyToMany(mappedBy = "keywords")
    private List<Review> reviews = new ArrayList<>();
}

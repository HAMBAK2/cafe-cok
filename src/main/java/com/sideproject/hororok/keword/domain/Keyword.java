package com.sideproject.hororok.keword.domain;

import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.keword.domain.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "keyword")
    private List<CafeReviewKeyword> cafeReviewKeywords = new ArrayList<>();
}

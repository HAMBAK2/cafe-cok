package com.sideproject.cafe_cok.keword.domain;

import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;

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

    protected Keyword() {
    }

    public Keyword(final String name, final Category category) {
        this.name = name;
        this.category = category;
    }
}

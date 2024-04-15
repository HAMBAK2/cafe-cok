package com.sideproject.hororok.category.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.global.entity.BaseEntity;
import com.sideproject.hororok.keword.domain.Keyword;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@Table(name = "categories")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @OneToMany(mappedBy = "category")
    private List<Keyword> keywords;
}
